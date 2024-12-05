package doan.doan_v1.dto;

import doan.doan_v1.entity.ComputerSoftware;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComputerSoftwareDto {
    private Integer id;
    private Integer computerId;
    private Integer softwareId;
    private String name; // Tên phần mềm
    private String version;
    private String description;
    private Integer status;
} 