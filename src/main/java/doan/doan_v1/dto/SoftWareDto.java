package doan.doan_v1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SoftWareDto implements Serializable {

    private int id;

    private String name;

    private String version;

    private LocalDateTime setupTime;

    private int status;

    private int computerId;

}
