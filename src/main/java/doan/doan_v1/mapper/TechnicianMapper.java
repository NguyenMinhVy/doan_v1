package doan.doan_v1.mapper;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.TechnicianLocation;
import doan.doan_v1.repository.LocationRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class, LocationMapper.class})
public abstract class TechnicianMapper {
    
    @Autowired
    protected LocationMapper locationMapper;
    
    @Autowired
    protected UserMapper userMapper;
    
    @Autowired
    protected LocationRepository locationRepository;
    
    @Mapping(target = "locationDtos", ignore = true)
    public abstract TechnicianDto technicianToTechnicianDto(Technician technician);
    
    @Mapping(target = "userId", source = "userDto.id")
    public abstract Technician technicianDtoToTechnician(TechnicianDto technicianDto);
    
    public TechnicianDto technicianToTechnicianDtoWithLocations(
            Technician technician, 
            List<TechnicianLocation> technicianLocations) {
        TechnicianDto dto = technicianToTechnicianDto(technician);
        
        if (technicianLocations != null) {
            List<LocationDto> locationDtos = technicianLocations.stream()
                .map(tl -> locationRepository.findById(tl.getLocationId())
                    .map(location -> locationMapper.locationToLocationDto(location))
                    .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            dto.setLocationDtos(locationDtos);
        }
        
        return dto;
    }
}
