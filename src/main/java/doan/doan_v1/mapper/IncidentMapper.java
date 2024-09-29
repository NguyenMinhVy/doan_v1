package doan.doan_v1.mapper;

import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.entity.Incident;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IncidentMapper {

    IncidentDto incidentToIncidentDto(Incident incident);

    Incident incidentDtoToIncident(IncidentDto incidentDto);

    List<IncidentDto> incidentListToIncidentDtoList(List<Incident> incidentList);
}
