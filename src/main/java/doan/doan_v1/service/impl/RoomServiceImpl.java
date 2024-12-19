package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.RoomMapper;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    @Lazy
    private ComputerService computerService;

    @Override
    public List<RoomDto> getAllRoomList() {
        List<Room> roomList = roomRepository.findByDelFlagFalse();
        if (roomList.isEmpty()) {
            return Collections.emptyList();
        }
        List<RoomDto> roomDtoList = new ArrayList<>();
        for (Room room : roomList) {
            RoomDto roomDto = roomMapper.roomToRoomDto(room);
            List<ComputerDto> computerDtoList = computerService.getComputerListByRoomId(roomDto.getId());
            int actualQuantity = 0;
            int status = Constant.STATUS.EMPTY;
            if (!computerDtoList.isEmpty()) {
                actualQuantity = computerDtoList
                        .stream()
                        .filter(computerDto -> computerDto.getStatus() != Constant.STATUS.EMPTY)
                        .toList()
                        .size();

                var computerDtoListError = computerDtoList
                        .stream()
                        .filter(computerDto -> computerDto.getStatus() == Constant.STATUS.ERROR)
                        .toList();

                if (actualQuantity != 0) {
                    status = computerDtoListError.isEmpty() ? Constant.STATUS.OK : Constant.STATUS.ERROR;
                } else {
                    status = Constant.STATUS.EMPTY;
                }
            }
            LocationDto locationDto = locationService.getLocationById(room.getLocationId());
            RoomTypeDto roomTypeDto = roomTypeService.getRoomTypeById(room.getRoomTypeId());
            roomDto.setLocationDto(locationDto);
            roomDto.setRoomTypeDto(roomTypeDto);
            roomDto.setActualQuantity(actualQuantity);
            roomDto.setStatus(status);
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
        roomDto.setTechnicianDto(technicianService.getTechnicianDtoById(room.getTechnicianId()));
        return roomDto;
    }

    @Override
    public RoomDto createRoom(RoomDto roomDto) {
        Room room = roomMapper.roomDtoToRoom(roomDto);
        room.setRoomTypeId(roomDto.getRoomTypeDto().getId());
        room.setLocationId(roomDto.getLocationDto().getId());
        room.setTechnicianId(roomDto.getTechnicianDto().getId());
        return roomMapper.roomToRoomDto(roomRepository.save(room));
    }

    @Override
    public Map<LocationDto, List<RoomDto>> getRoomsByLocation() {
        List<RoomDto> allRooms = getAllRoomList();

        Map<LocationDto, List<RoomDto>> roomsByLocation = new TreeMap<>(Comparator.comparing(LocationDto::getName));

        for (RoomDto room : allRooms) {
            roomsByLocation.computeIfAbsent(room.getLocationDto(), k -> new ArrayList<>()).add(room);
        }

        Comparator<RoomDto> roomComparator = (room1, room2) -> {
            String name1 = room1.getName();
            String name2 = room2.getName();

            String prefix1 = name1.replaceAll("\\d.*", "");
            String prefix2 = name2.replaceAll("\\d.*", "");

            if (!prefix1.equals(prefix2)) {
                return prefix1.compareTo(prefix2);
            }

            String numberPart1 = name1.replaceAll("\\D+", "");
            String numberPart2 = name2.replaceAll("\\D+", "");

            if (!numberPart1.isEmpty() && !numberPart2.isEmpty()) {
                int num1 = Integer.parseInt(numberPart1);
                int num2 = Integer.parseInt(numberPart2);
                return Integer.compare(num1, num2);
            }

            return name1.compareTo(name2);
        };

        for (List<RoomDto> rooms : roomsByLocation.values()) {
            rooms.sort(roomComparator);
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
        room.setTechnicianId(roomDto.getTechnicianDto().getId());
        return roomMapper.roomToRoomDto(roomRepository.save(room));
    }

    @Override
    public List<RoomDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.roomListToRoomDtoList(rooms);
    }
}