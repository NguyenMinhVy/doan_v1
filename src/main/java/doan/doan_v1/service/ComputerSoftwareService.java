package doan.doan_v1.service;

import doan.doan_v1.dto.ComputerSoftwareDto;
import java.util.List;

public interface ComputerSoftwareService {
    List<ComputerSoftwareDto> getSoftwareByComputerId(int computerId);

    ComputerSoftwareDto getSoftwareById(int id);
}