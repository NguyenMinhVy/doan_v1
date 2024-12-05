package doan.doan_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianStatisticsDto implements Serializable {
    private String technicianName;
    private int totalIncidents;
    private int unprocessed;
    private int processing;
    private int processed;
    private int overdueUnprocessed;
    private int overdueProcessed;

}
