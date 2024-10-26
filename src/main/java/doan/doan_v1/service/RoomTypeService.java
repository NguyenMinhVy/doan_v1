package doan.doan_v1.service;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.RoomType;

import java.util.List;

public interface RoomTypeService {
    RoomTypeDto getRoomTypeById(int id);

    List<RoomTypeDto> getAllRoomTypesSortedByName();
}
