package doan.doan_v1.service.impl;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.RoomMapper;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.RoomService;
import doan.doan_v1.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Override
    public List<RoomDto> getAllRoomList() {
        List<Room> roomList = roomRepository.findByDelFlagFalse();
        if (roomList.isEmpty()) {
            return Collections.emptyList();
        }
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDto roomDto = roomMapper.roomToRoomDto(room);
            LocationDto locationDto = locationService.getLocationById(room.getLocationId());
            RoomTypeDto roomTypeDto = roomTypeService.getRoomTypeById(room.getRoomTypeId());
            roomDto.setLocationDto(locationDto);
            roomDto.setRoomTypeDto(roomTypeDto);
            roomDtoList.add(roomDto);
        }
        return roomDtoList;
    }

    @Override
    public RoomDto getRoomById(int id) {
        Room room = roomRepository.findById(id).orElse(null);
        return roomMapper.roomToRoomDto(room);
    }
}