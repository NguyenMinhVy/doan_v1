package doan.doan_v1.service.impl;

import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.entity.Device;
import doan.doan_v1.mapper.DeviceMapper;
import doan.doan_v1.repository.DeviceRepository;
import doan.doan_v1.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceMapper deviceMapper;


    @Override
    public List<DeviceDto> findAllDeviceDtoByComputerId(int computerId) {

        List<Device> deviceList = deviceRepository.findByComputerIdAndDelFlagFalse(computerId);
        if (deviceList.isEmpty()) {
            return Collections.emptyList();
        }
        return deviceMapper.deviceListToDeviceDtoList(deviceList);
    }

    @Override
    public DeviceDto findDeviceDtoById(int deviceId) {
        Device device = deviceRepository.findById(deviceId).orElse(null);
        return deviceMapper.deviceToDeviceDto(device);
    }
}
