package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.entity.*;
import doan.doan_v1.mapper.ComputerMapper;
import doan.doan_v1.mapper.DeviceMapper;
import doan.doan_v1.mapper.SoftWareMapper;
import doan.doan_v1.repository.ComputerDeviceRepository;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.ComputerSoftWareRepository;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;

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

        return computerMapper.computerToComputerDto(computer);
    }

    @Override
    public boolean isComputerNameExist(String name) {
        Computer computer = computerRepository.findComputerByNameAndDelFlagFalse(name);
        return computer != null;
    }

    @Override
    public ComputerDto createComputer(ComputerDto computerDto) {
        Computer computer = computerMapper.computerDtoToComputer(computerDto);
        computerRepository.save(computer);
        computer.setId(computer.getId());
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        List<SoftWareDto> softwareDtoList = new ArrayList<>();
        for (Integer deviceId : computerDto.getDeviceIdList()) {
            DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
            deviceDtoList.add(deviceDto);
        }
        for (Integer softWareId : computerDto.getSoftWareIdList()) {
            SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softWareId);
            softWareDto.setStatus(Constant.STATUS.OK);
            softwareDtoList.add(softWareDto);
        }

        for (DeviceDto device : deviceDtoList) {
            ComputerDevice computerDevice = new ComputerDevice();
            computerDevice.setDeviceId(device.getId());
            computerDevice.setComputerId(computer.getId());

            int type = device.getType();
            switch (type) {
                case Constant.DEVICE_TYPE.KEY :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.KEY + "-" + computer.getId() + "-" + device.getId());
                    break;
                case Constant.DEVICE_TYPE.MOU :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.MOU + "-" + computer.getId() + "-" + device.getId());
                    break;
                case Constant.DEVICE_TYPE.SCREEN :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.SCREEN + "-" + computer.getId() + "-" + device.getId());
                    break;
                case Constant.DEVICE_TYPE.CASE :
                    computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.CASE + "-" + computer.getId() + "-" + device.getId());
                    break;
            }
            computerDeviceRepository.save(computerDevice);
        }
        for (SoftWareDto software : softwareDtoList) {
            ComputerSoftware computerSoftware = new ComputerSoftware();
            computerSoftware.setSoftwareId(software.getId());
            computerSoftware.setComputerId(computer.getId());
            computerSoftware.setStatus(Constant.STATUS.OK);
            computerSoftWareRepository.save(computerSoftware);
        }
        return computerMapper.computerToComputerDto(computer);
    }
}
