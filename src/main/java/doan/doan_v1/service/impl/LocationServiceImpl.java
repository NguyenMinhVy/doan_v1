package doan.doan_v1.service.impl;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.entity.Location;
import doan.doan_v1.mapper.LocationMapper;
import doan.doan_v1.repository.LocationRepository;
import doan.doan_v1.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMapper locationMapper;

    @Override
    public LocationDto getLocationById(int id) {
        Location location = locationRepository.findByIdAndDelFlagFalse(id);
        if (location == null) {
            return null;
        }
        return locationMapper.locationToLocationDto(location);
    }
}
