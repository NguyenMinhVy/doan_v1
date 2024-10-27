package doan.doan_v1.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TechnicianDto extends AbstractDto implements Serializable {
    private int id;

    private String technicianCode;

    private UserDto userDto;

    private LocationDto locationDto;
}
