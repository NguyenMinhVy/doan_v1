package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.ComputerDeviceDto;
import doan.doan_v1.entity.ComputerDevice;
import doan.doan_v1.mapper.ComputerDeviceMapper;
import doan.doan_v1.repository.ComputerDeviceRepository;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.service.ComputerDeviceService;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerDeviceServiceImpl implements ComputerDeviceService {

    @Autowired
    private ComputerDeviceRepository computerDeviceRepository;
    @Autowired
    private ComputerDeviceMapper computerDeviceMapper;
    @Autowired
    private ComputerRepository computerRepository;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public List<ComputerDeviceDto> getDevicesByComputerId(int computerId) {
        List<ComputerDevice> computerDevices = computerDeviceRepository.findByComputerId(computerId);
        List<ComputerDeviceDto> computerDeviceDtos = new ArrayList<>();
        for (ComputerDevice computerDevice : computerDevices){
            ComputerDeviceDto computerDeviceDto = new ComputerDeviceDto();
            computerDeviceDto = computerDeviceMapper.computerDeviceToComputerDeviceDto(computerDevice);
            computerDeviceDto.setType(Constant.getDeviceTypeName(deviceService.findDeviceDtoById(computerDevice.getDeviceId()).getType()));
            computerDeviceDtos.add(computerDeviceDto);
        }
        return computerDeviceDtos;
    }

    @Override
    public ComputerDeviceDto getDeviceById(int id) {
        ComputerDevice computerDevice = computerDeviceRepository.findById(id).orElse(null);
        ComputerDeviceDto computerDeviceDto = computerDeviceMapper.computerDeviceToComputerDeviceDto(computerDevice);
        assert computerDevice != null;
        computerDeviceDto.setType(Constant.getDeviceTypeName(deviceService.findDeviceDtoById(computerDevice.getDeviceId()).getType()));

        return computerDeviceDto;
    }

} 