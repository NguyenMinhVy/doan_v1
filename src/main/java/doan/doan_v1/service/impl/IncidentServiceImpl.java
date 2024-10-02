package doan.doan_v1.service.impl;

import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.entity.Incident;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.IncidentMapper;
import doan.doan_v1.repository.IncidentRepository;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
//        incidentDto.set(userService.getUserInfo().getId());
        return incidentMapper.incidentToIncidentDto(incidentRepository.save(incidentMapper.incidentDtoToIncident(incidentDto)));
    }
}
