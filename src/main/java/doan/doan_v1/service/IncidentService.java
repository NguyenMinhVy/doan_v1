package doan.doan_v1.service;

import doan.doan_v1.dto.IncidentDto;

import java.util.List;

public interface IncidentService {

    List<IncidentDto> getIncidentDtoListByDeviceId(int deviceId);

    List<IncidentDto> getIncidentDtoListBySoftwareId(int softwareId);

    IncidentDto addIncident(IncidentDto incidentDto);

    IncidentDto addIncidentForComputer(IncidentDto incidentDto);

    List<IncidentDto> getIncidentDtoList();

    List<IncidentDto> getIncidentDtoListByComputerId(int computerId);

    List<IncidentDto> getIncidentDtoListByStatus(List<IncidentDto> incidentDtoList, int status);

    IncidentDto getIncidentDtoById(Integer id);

    void updateIncident(Integer id, IncidentDto incidentDto);

    void updateIncident(IncidentDto incidentDto);

    IncidentDto getLatestIncidentByComputerDeviceId(int computerDeviceId);

    IncidentDto getLatestIncidentByComputerSoftwareId(int computerSoftwareId);

    List<IncidentDto> getIncidentsByLocationAndTechnician(Integer locationId, Integer technicianId);

}
