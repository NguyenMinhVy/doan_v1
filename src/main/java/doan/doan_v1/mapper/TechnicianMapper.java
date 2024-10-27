package doan.doan_v1.mapper;

import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Technician;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TechnicianMapper {

    TechnicianDto technicianToTechnicianDto(Technician technician);
    Technician technicianDtoToTechnician(TechnicianDto technicianDto);
}
