package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.entity.*;
import doan.doan_v1.mapper.ComputerMapper;
import doan.doan_v1.mapper.DeviceMapper;
import doan.doan_v1.mapper.SoftWareMapper;
import doan.doan_v1.repository.ComputerDeviceRepository;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.ComputerSoftWareRepository;
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

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private SoftWareMapper softWareMapper;

    @Autowired
    private ComputerDeviceRepository computerDeviceRepository;

    @Autowired
    private ComputerSoftWareRepository computerSoftWareRepository;

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

    @Override
    public boolean isComputerNameExist(String name) {
        Computer computer = computerRepository.findComputerByNameAndDelFlagFalse(name);
        return computer != null;
    }

    @Override
    public ComputerDto createComputer(ComputerDto computerDto) {
        Computer computer = computerMapper.computerDtoToComputer(computerDto);
        List<Device> deviceList = deviceMapper.deviceDtoListToDeviceList(computerDto.getDeviceDtoList());
        List<Software> softwareList = softWareMapper.softWareDtoListToSoftWareList(computerDto.getSoftWareDtoList());
        ComputerDevice computerDevice = new ComputerDevice();
        ComputerSoftware computerSoftware = new ComputerSoftware();
        for (Device device : deviceList) {
            computerDevice.setDeviceId(device.getId());
            computerDevice.setComputerId(computer.getId());

            int type = device.getType();
            switch (type) {
                case Constant.DEVICE_TYPE.KEY :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.KEY + "-" + computer.getId() + "-" + device.getId());
                case Constant.DEVICE_TYPE.MOU :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.MOU + "-" + computer.getId() + "-" + device.getId());
                case Constant.DEVICE_TYPE.SCREEN :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.SCREEN + "-" + computer.getId() + "-" + device.getId());
                case Constant.DEVICE_TYPE.CASE :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.CASE + "-" + computer.getId() + "-" + device.getId());
            }
            computerDeviceRepository.save(computerDevice);
        }
        for (Software software : softwareList) {
            computerSoftware.setSoftwareId(software.getId());
            computerSoftware.setComputerId(computer.getId());
            computerSoftWareRepository.save(computerSoftware);
        }
        computerRepository.save(computer);
        return computerMapper.computerToComputerDto(computer);
    }
}
