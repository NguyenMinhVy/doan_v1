package doan.doan_v1.service.impl;

import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.entity.Computer;
import doan.doan_v1.mapper.ComputerMapper;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.service.ComputerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
@Service
public class ComputerServiceImpl implements ComputerService {

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private ComputerMapper computerMapper;

    @Override
    public List<ComputerDto> getComputerListByRoomId(int roomId) {
        List<Computer> computerList = computerRepository.findByRoomIdAndDelFlagFalse(roomId);
        if (computerList.isEmpty()) {
            return Collections.emptyList();
        }
        return computerMapper.computerListToComputerDtoList(computerList);
    }

    @Override
    public ComputerDto getComputerById(int id) {

        Computer computer = computerRepository.findById(id).orElse(null);
        if (computer == null) {
            return null;
        }

        return null;
    }
}
