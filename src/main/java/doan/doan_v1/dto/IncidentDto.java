package doan.doan_v1.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class IncidentDto implements Serializable {

    private int id;

    private String description;

    private int status;

    private LocalDateTime reportDate;

    private int reportUser;

    private int softwareId;

    private int deviceId;
}
