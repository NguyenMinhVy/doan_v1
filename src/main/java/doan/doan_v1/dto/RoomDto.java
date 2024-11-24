package doan.doan_v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RoomDto extends AbstractDto implements Serializable {

    private int id;

    private String name;

    private LocationDto locationDto;

    private RoomTypeDto roomTypeDto;

    private List<ComputerDto> computerDtoList;

    private TechnicianDto technicianDto;
}
