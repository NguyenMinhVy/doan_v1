package doan.doan_v1.dto;

import doan.doan_v1.entity.Incident;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentDto extends AbstractDto implements Serializable {

    private int id;

    private String description;

    private int status;

    private LocalDateTime reportDate;

    private LocalDateTime expectCompleteDate;

    private LocalDateTime completedDate;

    private int reportUser;

    private String reportUserName;

    private Integer computerDeviceId;
    private Integer computerSoftwareId;
    private ComputerDeviceDto computerDeviceDto;
    private ComputerSoftwareDto computerSoftwareDto;

    private int computerId;

    private int locationId;

    private int technicianId;

    private TechnicianDto technicianDto;

    private String computerName;

    private String unprocessedReason;

}
