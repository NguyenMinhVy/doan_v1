package doan.doan_v1.mapper;

import doan.doan_v1.dto.ComputerSoftwareDto;
import doan.doan_v1.entity.ComputerSoftware;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComputerSoftwareMapper {

    ComputerSoftware ComputerSoftwareDtoToComputerSoftware(ComputerSoftwareDto ComputerSoftwareDto);

    ComputerSoftwareDto ComputerSoftwareToComputerSoftwareDto(ComputerSoftware ComputerSoftware);

    List<ComputerSoftware> ComputerSoftwareDtoListToComputerSoftwareList(List<ComputerSoftwareDto> ComputerSoftwareDtoList);

    List<ComputerSoftwareDto> ComputerSoftwareListToComputerSoftwareDtoList(List<ComputerSoftware> ComputerSoftwareList);
}
