package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.ComputerDeviceDto;
import doan.doan_v1.dto.ComputerSoftwareDto;
import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Computer;
import doan.doan_v1.entity.Incident;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.IncidentMapper;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.IncidentRepository;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private ComputerService computerService;

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private ComputerDeviceService computerDeviceService;

    @Autowired
    private ComputerSoftwareService computerSoftwareService;

    @Autowired
    private TechnicianRepository technicianRepository;
    private ComputerDeviceServiceImpl computerDeviceServiceImpl;


    @Override
    public List<IncidentDto> getIncidentDtoListByDeviceId(int deviceId) {
        List<Incident> incidentList = incidentRepository.findByComputerDeviceIdAndDelFlagFalse(deviceId);
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return incidentMapper.incidentListToIncidentDtoList(incidentList);
    }

    @Override
    public List<IncidentDto> getIncidentDtoListBySoftwareId(int softwareId) {
        List<Incident> incidentList = incidentRepository.findByComputerSoftwareIdAndDelFlagFalse(softwareId);
        return getIncidentDtos(incidentList);
    }

    private List<IncidentDto> getIncidentDtos(List<Incident> incidentList) {
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        List<IncidentDto> incidentDtoList = new ArrayList<>();
        for (Incident incident : incidentList) {
            IncidentDto incidentDto = incidentMapper.incidentToIncidentDto(incident);
            if (incident.getTechnicianId() != 0) {
                TechnicianDto technicianDto = technicianService.getTechnicianDtoById(incident.getTechnicianId());
                incidentDto.setTechnicianDto(technicianDto);
                incidentDto.setTechnicianId(incident.getTechnicianId());
            }
            String computerName = computerService.getComputerById(incident.getComputerId()).getName();
            incidentDto.setComputerName(computerName);

            if (incident.getComputerDeviceId()>0){
                ComputerDeviceDto computerDeviceDto = computerDeviceService.getDeviceById(incident.getComputerDeviceId());
                incidentDto.setComputerDeviceDto(computerDeviceDto);
            }

            if (incident.getComputerSoftwareId()>0){
                ComputerSoftwareDto computerSoftwareDto = computerSoftwareService.getSoftwareById(incident.getComputerSoftwareId());
                incidentDto.setComputerSoftwareDto(computerSoftwareDto);
            }

            incidentDtoList.add(incidentDto);
        }
        return incidentDtoList.stream()
                .sorted(Comparator
                        .comparing(IncidentDto::getReportDate, Comparator.reverseOrder())
                        .thenComparing(IncidentDto::getStatus)
                        .thenComparing(IncidentDto::getComputerName, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public IncidentDto addIncident(IncidentDto incidentDto) {
        User user = userRepository.findById(incidentDto.getReportUser()).orElse(null);
        if (user == null) {
            return null;
        }
        incidentDto.setReportUserName(user.getUsername());
        return incidentMapper.incidentToIncidentDto(incidentRepository.save(incidentMapper.incidentDtoToIncident(incidentDto)));
    }

    @Override
    public IncidentDto addIncidentForComputer(IncidentDto incidentDto) {
        Incident incident = incidentMapper.incidentDtoToIncident(incidentDto);
        incident.setTechnicianId(incidentDto.getTechnicianId());
        incidentRepository.save(incident);

        Computer computer = computerRepository.findById(incidentDto.getComputerId()).orElse(null);
        assert computer != null;
        computer.setStatus(Constant.STATUS.ERROR);
        computerRepository.save(computer);
        return incidentDto;
    }

    @Override
    public List<IncidentDto> getIncidentDtoList() {
        List<Incident> incidentList = incidentRepository.findAll();
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return getIncidentDtos(incidentList);
    }

    @Override
    public List<IncidentDto> getIncidentDtoListByComputerId(int computerId) {
        List<Incident> incidentList = incidentRepository.findByComputerIdAndDelFlagFalse(computerId);
        return getIncidentDtos(incidentList);
    }

    @Override
    public List<IncidentDto> getIncidentDtoListByStatus(List<IncidentDto> incidentDtoList, int status) {
        return incidentDtoList.stream()
                .filter(incidentDto -> incidentDto.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public IncidentDto getIncidentDtoById(Integer id) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự cố với ID: " + id));
        IncidentDto incidentDto = incidentMapper.incidentToIncidentDto(incident);
        TechnicianDto technicianDto = technicianService.getTechnicianDtoById(incident.getTechnicianId());
        String computerName = computerService.getComputerById(incident.getComputerId()).getName();
        incidentDto.setComputerName(computerName);
        incidentDto.setTechnicianId(technicianDto.getId());
        incidentDto.setTechnicianDto(technicianDto);
        return incidentDto;
    }

    @Override
    @Transactional
    public void updateIncident(Integer id, IncidentDto incidentDto) {
        Incident incident = incidentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sự cố với ID: " + id));
        
        // Cập nhật các trường thông tin
        incident.setStatus(incidentDto.getStatus());
        incident.setUnprocessedReason(incidentDto.getUnprocessedReason());
        
        // Nếu có thay đổi kỹ thuật viên
        if (incidentDto.getTechnicianId() != 0) {
            Technician technician = technicianRepository.findById(incidentDto.getTechnicianId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy kỹ thuật viên"));
            incident.setTechnicianId(technician.getId());
        }
        
        // Cập nhật thời gian hoàn thành nếu trạng thái là đã hoàn thành
        if (incidentDto.getStatus() == Constant.INCIDENT_STATUS.PROCESSED ||
            incidentDto.getStatus() == Constant.INCIDENT_STATUS.OVERDUE_PROCESSED) {
            incident.setCompletedDate(LocalDateTime.now());
        }
        
        incidentRepository.save(incident);
        List<Incident> incidents = incidentRepository.findAllByComputerIdAndDelFlagFalse(incident.getComputerId());

        boolean isUpdateStatusComputer = true;
        for (Incident item : incidents){
            if ((item.getStatus() != Constant.INCIDENT_STATUS.PROCESSED) && (item.getStatus() != Constant.INCIDENT_STATUS.OVERDUE_PROCESSED) ){
                isUpdateStatusComputer = false;
                break;
            }
        }
        if (isUpdateStatusComputer){
            Computer computer = computerRepository.findById(incident.getComputerId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy máy tính với ID: " + incident.getComputerId()));
            computer.setStatus(Constant.STATUS.OK);
            computerRepository.save(computer);
        }

    }

    @Override
    public void updateIncident(IncidentDto incidentDto) {
        Incident incident = incidentMapper.incidentDtoToIncident(incidentDto);
        incidentRepository.save(incident);
    }

    @Override
    public IncidentDto getLatestIncidentByComputerDeviceId(int computerDeviceId) {
        Optional<Incident> latestIncident = incidentRepository
            .findTopByComputerDeviceIdOrderByReportDateDesc(computerDeviceId);
        
        return latestIncident.map(incidentMapper::incidentToIncidentDto).orElse(null);
    }

    @Override
    public IncidentDto getLatestIncidentByComputerSoftwareId(int computerSoftwareId) {
        Optional<Incident> latestIncident = incidentRepository
            .findTopByComputerSoftwareIdOrderByReportDateDesc(computerSoftwareId);
        
        return latestIncident.map(incidentMapper::incidentToIncidentDto).orElse(null);
    }

//    @Override
//    public List<IncidentDto> getIncidentsByComputerId(int computerId) {
//        return List.of();
//    }

}
