package doan.doan_v1.mapper;

import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.entity.Computer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComputerMapper {

    Computer computerDtoToComputer(ComputerDto computerDto);

    ComputerDto computerToComputerDto(Computer computer);

    List<Computer> computerDtoListToComputerList(List<ComputerDto> computerDtoList);

    List<ComputerDto> computerListToComputerDtoList(List<Computer> computerList);
}
