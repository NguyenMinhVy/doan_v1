package doan.doan_v1.mapper;

import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.entity.Device;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    DeviceDto deviceToDeviceDto(Device device);

    Device deviceDtoToDevice(DeviceDto deviceDto);

    List<DeviceDto> deviceListToDeviceDtoList(List<Device> devices);

    List<Device> deviceDtoListToDeviceList(List<DeviceDto> deviceDtos);
}
