package doan.doan_v1.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class RoomTypeDto implements Serializable {
    private int id;

    private String name;

    private String capacity;
}
