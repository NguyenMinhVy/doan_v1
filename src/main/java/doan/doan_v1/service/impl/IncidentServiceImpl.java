package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
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
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
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
    private TechnicianRepository technicianRepository;


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
            TechnicianDto technicianDto = technicianService.getTechnicianDtoById(incident.getTechnicianId());
            String computerName = computerService.getComputerById(incident.getComputerId()).getName();
            incidentDto.setComputerName(computerName);
            incidentDto.setTechnicianDto(technicianDto);
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
        incident.setTechnicianId(incidentDto.getTechnicianDto().getId());
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
                
        return incidentMapper.incidentToIncidentDto(incident);
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
        if (incidentDto.getTechnicianDto() != null) {
            Technician technician = technicianRepository.findById(incidentDto.getTechnicianDto().getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy kỹ thuật viên"));
            incident.setTechnicianId(technician.getUserId());
        }
        
        // Cập nhật thời gian hoàn thành nếu trạng thái là đã hoàn thành
        if (incidentDto.getStatus() == Constant.INCIDENT_STATUS.PROCESSED ||
            incidentDto.getStatus() == Constant.INCIDENT_STATUS.OVERDUE_PROCESSED) {
            incident.setCompletedDate(LocalDateTime.now());
        }
        
        incidentRepository.save(incident);
    }

}
