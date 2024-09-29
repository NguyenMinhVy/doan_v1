package doan.doan_v1.mapper;

import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.entity.Software;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SoftWareMapper {

    Software softWareDtoToSoftWare (SoftWareDto softWareDto);

    SoftWareDto softWareToSoftWareDto(Software software);
}
