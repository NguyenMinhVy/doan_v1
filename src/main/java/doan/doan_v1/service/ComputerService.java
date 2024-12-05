package doan.doan_v1.service;

import doan.doan_v1.dto.ComputerDto;

import java.util.List;

public interface ComputerService {

    List<ComputerDto> getComputerListByRoomId(int roomId);

    ComputerDto getComputerById(int id);

    boolean isComputerNameExist (String name);

    ComputerDto createComputer(ComputerDto computerDto);

    List<ComputerDto> createManyComputer(ComputerDto computerDto);

    void updateComputer(int id, ComputerDto computerDto);

    void deleteComputer(int id);

    void updateComputerNameByRoom(Integer computerId, String newRoomName);
}
