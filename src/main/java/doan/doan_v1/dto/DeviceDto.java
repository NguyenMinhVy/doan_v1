package doan.doan_v1.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class DeviceDto extends AbstractDto implements Serializable {
    private int id;

    private String name;

    private int type;

    private int status;

    private String deviceCode;

    private int computerId;

}
