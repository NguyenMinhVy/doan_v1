package doan.doan_v1.mapper;

import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.RoomType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    RoomType roomTypeDtoToRoomType(RoomTypeDto roomTypeDto);

    RoomTypeDto roomTypeToRoomTypeDto(RoomType roomType);

}
