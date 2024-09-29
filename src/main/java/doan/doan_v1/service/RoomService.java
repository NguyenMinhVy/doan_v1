package doan.doan_v1.service;

import doan.doan_v1.dto.RoomDto;

import java.util.List;

public interface RoomService {

    List<RoomDto> getAllRoomList();

    RoomDto getRoomById(int id);
}
