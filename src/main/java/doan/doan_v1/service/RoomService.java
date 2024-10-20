package doan.doan_v1.service;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;

import java.util.List;
import java.util.Map;

public interface RoomService {

    List<RoomDto> getAllRoomList();

    RoomDto getRoomById(int id);

    RoomDto createRoom(RoomDto roomDto);

    Map<LocationDto, List<RoomDto>> getRoomsByLocation();
}
