package doan.doan_v1.mapper;

import doan.doan_v1.dto.ComputerDeviceDto;
import doan.doan_v1.entity.ComputerDevice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComputerDeviceMapper {

    ComputerDevice computerDeviceDtoToComputerDevice(ComputerDeviceDto computerDeviceDto);

    ComputerDeviceDto computerDeviceToComputerDeviceDto(ComputerDevice computerDevice);

    List<ComputerDevice> computerDeviceDtoListToComputerDeviceList(List<ComputerDeviceDto> computerDeviceDtoList);

    List<ComputerDeviceDto> computerDeviceListToComputerDeviceDtoList(List<ComputerDevice> computerDeviceList);
}
