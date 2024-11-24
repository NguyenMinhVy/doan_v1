package doan.doan_v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LecturerDto extends AbstractDto implements Serializable {
    private int id;
    private String lecturerCode;
    private UserDto userDto;
}