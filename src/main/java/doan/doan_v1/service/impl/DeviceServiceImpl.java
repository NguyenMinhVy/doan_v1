package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.entity.ComputerDevice;
import doan.doan_v1.entity.Device;
import doan.doan_v1.mapper.DeviceMapper;
import doan.doan_v1.repository.ComputerDeviceRepository;
import doan.doan_v1.repository.DeviceRepository;
import doan.doan_v1.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private ComputerDeviceRepository computerDeviceRepository;



    @Override
    public List<DeviceDto> findAllDeviceDtoByComputerId(int computerId) {
        List<ComputerDevice> computerDeviceList = computerDeviceRepository.findByComputerId(computerId);
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        for (ComputerDevice computerDevice : computerDeviceList) {
            DeviceDto deviceDto = findDeviceDtoById(computerDevice.getDeviceId());
            deviceDto.setDeviceCode(computerDevice.getDeviceCode());
            deviceDto.setComputerId(computerDevice.getComputerId());
            deviceDtoList.add(deviceDto);
        }
        return deviceDtoList;
    }

    @Override
    public DeviceDto findDeviceDtoById(int deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        return deviceMapper.deviceToDeviceDto(device);
    }

    @Override
    public DeviceDto updateDeviceDto(DeviceDto deviceDto) {
        Device device = deviceRepository.findById(deviceDto.getId()).orElse(null);
        if (device == null) {
            return null;
        }else {
            Device deviceUpdate = deviceMapper.deviceDtoToDevice(deviceDto);
            deviceUpdate.setId(deviceDto.getId());
            deviceRepository.save(deviceUpdate);
        }
        return deviceDto;
    }

    @Override
    public List<DeviceDto> findAllDeviceDtoList() {
        List<Device> deviceList = deviceRepository.findByDelFlagFalse();
        if (deviceList.isEmpty()) {
            return Collections.emptyList();
        }
        return deviceMapper.deviceListToDeviceDtoList(deviceList);
    }
}
