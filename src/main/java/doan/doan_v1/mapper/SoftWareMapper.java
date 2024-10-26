package doan.doan_v1.mapper;

import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.entity.Software;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SoftWareMapper {

    Software softWareDtoToSoftWare (SoftWareDto softWareDto);

    SoftWareDto softWareToSoftWareDto(Software software);

    List<Software> softWareDtoListToSoftWareList (List<SoftWareDto> softWareDtoList);

    List<SoftWareDto> softWareListToSoftWareDtoList (List<Software> softWareList);
}
