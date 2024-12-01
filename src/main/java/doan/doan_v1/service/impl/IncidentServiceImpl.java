package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Computer;
import doan.doan_v1.entity.Incident;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.IncidentMapper;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.IncidentRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return incidentMapper.incidentListToIncidentDtoList(incidentList);
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

        return getTechnicianDtoToIncidentDto(incidentList);
    }

    private List<IncidentDto> getTechnicianDtoToIncidentDto(List<Incident> incidentList) {
        List<IncidentDto> incidentDtoList = new ArrayList<>();
        for (Incident incident : incidentList) {
            IncidentDto incidentDto = incidentMapper.incidentToIncidentDto(incident);
            TechnicianDto technicianDto = technicianService.getTechnicianDtoById(incident.getTechnicianId());
            String computerName = computerService.getComputerById(incident.getComputerId()).getName();
            incidentDto.setComputerName(computerName);
            incidentDto.setTechnicianDto(technicianDto);
            incidentDtoList.add(incidentDto);
        }
        return incidentDtoList;
    }

    @Override
    public List<IncidentDto> getIncidentDtoListByComputerId(int computerId) {
        List<Incident> incidentList = incidentRepository.findByComputerIdAndDelFlagFalse(computerId);
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return getTechnicianDtoToIncidentDto(incidentList);
    }

    @Override
    public List<IncidentDto> getIncidentDtoListByStatus(List<IncidentDto> incidentDtoList, int status) {
        return incidentDtoList.stream()
                .filter(incidentDto -> incidentDto.getStatus() == status)
                .collect(Collectors.toList());
    }


}
