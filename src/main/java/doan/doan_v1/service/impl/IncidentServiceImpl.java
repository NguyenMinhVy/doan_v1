package doan.doan_v1.service.impl;

import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.entity.Incident;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.IncidentMapper;
import doan.doan_v1.repository.IncidentRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class IncidentServiceImpl implements IncidentService {

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<IncidentDto> getIncidentDtoListByDeviceId(int deviceId) {
        List<Incident> incidentList = incidentRepository.findByDeviceIdAndDelFlagFalse(deviceId);
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return incidentMapper.incidentListToIncidentDtoList(incidentList);
    }

    @Override
    public List<IncidentDto> getIncidentDtoListBySoftwareId(int softwareId) {
        List<Incident> incidentList = incidentRepository.findBySoftwareIdAndDelFlagFalse(softwareId);
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
    public List<IncidentDto> getIncidentDtoList() {
        List<Incident> incidentList = incidentRepository.findAll();
        if (incidentList.isEmpty()) {
            return Collections.emptyList();
        }
        return incidentMapper.incidentListToIncidentDtoList(incidentList);
    }
}
