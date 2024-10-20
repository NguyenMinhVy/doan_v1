package doan.doan_v1.service;

import doan.doan_v1.dto.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto getLocationById(int id);

    List<LocationDto> getAllLocationsSortedByName();

}
