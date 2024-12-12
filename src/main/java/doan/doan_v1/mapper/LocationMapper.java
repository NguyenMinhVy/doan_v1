package doan.doan_v1.mapper;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationDtoToLocation(LocationDto locationDto);

    LocationDto locationToLocationDto(Location location);
    List<LocationDto> locationsToLocationDtos(List<Location> locations);
}
