package doan.doan_v1.mapper;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location locationDtoToLocation(LocationDto locationDto);

    LocationDto locationToLocationDto(Location location);
}
