package doan.doan_v1.service;

import doan.doan_v1.dto.DeviceDto;

import java.util.List;

public interface DeviceService {

    List<DeviceDto> findAllDeviceDtoByComputerId(int computerId);

    DeviceDto findDeviceDtoById(int deviceId);

    DeviceDto updateDeviceDto(DeviceDto deviceDto);
}
