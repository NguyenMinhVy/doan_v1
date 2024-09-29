package doan.doan_v1.service;

import doan.doan_v1.dto.ComputerDto;

import java.util.List;

public interface ComputerService {

    List<ComputerDto> getComputerListByRoomId(int roomId);

    ComputerDto getComputerById(int id);
}
