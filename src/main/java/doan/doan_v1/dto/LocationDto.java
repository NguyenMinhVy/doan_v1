package doan.doan_v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class LocationDto extends AbstractDto implements Serializable {

    private int id;
    private String name;
}
