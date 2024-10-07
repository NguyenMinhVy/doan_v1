package doan.doan_v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class ComputerDto extends AbstractDto implements Serializable {

    private int id;

    private String name;

    private int roomId;

    private int status;

    private List<DeviceDto> deviceDtoList;

    private List<SoftWareDto> softWareDtoList;
}
