package doan.doan_v1.service;

import doan.doan_v1.dto.IncidentDto;

import java.util.List;

public interface IncidentService {

    List<IncidentDto> getIncidentDtoListByDeviceId(int deviceId);

    List<IncidentDto> getIncidentDtoListBySoftwareId(int softwareId);


}
