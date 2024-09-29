package doan.doan_v1.mapper;

import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.entity.Room;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    Room roomDtoToRoom(RoomDto roomDto);

    RoomDto roomToRoomDto(Room room);

    List<RoomDto> roomListToRoomDtoList(List<Room> roomList);

    List<Room> roomDtoListToRoomList(List<RoomDto> roomDtoList);
}
