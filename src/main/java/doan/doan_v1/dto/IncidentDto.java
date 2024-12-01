package doan.doan_v1.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class IncidentDto extends AbstractDto implements Serializable {

    private int id;

    private String description;

    private int status;

    private LocalDateTime reportDate;

    private LocalDateTime expectCompleteDate;

    private LocalDateTime completedDate;

    private int reportUser;

    private String reportUserName;

    private int computerSoftwareId;

    private int computerDeviceId;

    private int computerId;

    private int locationId;

    private TechnicianDto technicianDto;

    private String computerName;

    private String unprocessedReason;
}
