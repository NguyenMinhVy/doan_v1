package doan.doan_v1.service.impl;

import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.RoomType;
import doan.doan_v1.mapper.RoomTypeMapper;
import doan.doan_v1.repository.RoomTypeRepository;
import doan.doan_v1.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private RoomTypeMapper roomTypeMapper;


    @Override
    public RoomTypeDto getRoomTypeById(int id) {
        RoomType roomType = roomTypeRepository.findByIdAndDelFlagFalse(id);
        if (roomType == null) {
            return null;
        }
        return roomTypeMapper.roomTypeToRoomTypeDto(roomType);
    }
}
