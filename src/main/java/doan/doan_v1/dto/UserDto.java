package doan.doan_v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto extends AbstractDto implements Serializable {

    private int id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String name;

    private int roleId;
}
