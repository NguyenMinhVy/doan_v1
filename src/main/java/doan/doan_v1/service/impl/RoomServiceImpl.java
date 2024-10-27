package doan.doan_v1.service.impl;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.Location;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.RoomMapper;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.RoomService;
import doan.doan_v1.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        RoomDto roomDto= roomMapper.roomToRoomDto(room);
        roomDto.setLocationDto(locationService.getLocationById(room.getLocationId()));
        roomDto.setRoomTypeDto(roomTypeService.getRoomTypeById(room.getRoomTypeId()));
        return roomDto;
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        Room room = roomMapper.roomDtoToRoom(roomDto);
        room.setRoomTypeId(roomDto.getRoomTypeDto().getId());
        room.setLocationId(roomDto.getLocationDto().getId());
        return roomMapper.roomToRoomDto(roomRepository.save(room));
    }

    @Override
    public Map<LocationDto, List<RoomDto>> getRoomsByLocation() {
        List<RoomDto> allRooms = getAllRoomList();

        Map<LocationDto, List<RoomDto>> roomsByLocation = new TreeMap<>(Comparator.comparing(LocationDto::getName));

        for (RoomDto room : allRooms) {
            roomsByLocation.computeIfAbsent(room.getLocationDto(), k -> new ArrayList<>()).add(room);
        }

        for (List<RoomDto> rooms : roomsByLocation.values()) {
            rooms.sort(Comparator.comparing(RoomDto::getName));
        }

        return roomsByLocation;
    }

    @Override
    public boolean isRoomNameExist(String name) {
        Room room = roomRepository.findRoomByNameAndDelFlagFalse(name);
        return room != null;
    }

    @Override
    public RoomDto updateRoom(RoomDto roomDto) {
        Room room = roomMapper.roomDtoToRoom(roomDto);
        room.setRoomTypeId(roomDto.getRoomTypeDto().getId());
        room.setLocationId(roomDto.getLocationDto().getId());
        return roomMapper.roomToRoomDto(roomRepository.save(room));
    }
}