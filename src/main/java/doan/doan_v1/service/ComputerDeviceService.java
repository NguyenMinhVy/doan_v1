package doan.doan_v1.service;

import doan.doan_v1.dto.ComputerDeviceDto;
import java.util.List;

public interface ComputerDeviceService {
    List<ComputerDeviceDto> getDevicesByComputerId(int computerId);

    ComputerDeviceDto getDeviceById(int id);
}