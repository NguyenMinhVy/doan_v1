package doan.doan_v1.dto;

import doan.doan_v1.entity.ComputerDevice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComputerDeviceDto {
    private Integer id;
    private Integer computerId;
    private Integer deviceId;
    private String type; // Loại thiết bị
    private String description;
    private Integer status;
} 