package doan.doan_v1.service.impl;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.entity.Location;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.LocationMapper;
import doan.doan_v1.repository.LocationRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<LocationDto> getAllLocationsSortedByName() {
        return locationRepository.findAllByOrderByNameAsc().stream()
                .map(locationMapper::locationToLocationDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public LocationDto getLocationByRoomId(int roomId) {
//        RoomDto roomDto = roomService.getRoomById(roomId);
//        return roomDto.getLocationDto();
//    }
}
