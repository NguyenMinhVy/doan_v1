package doan.doan_v1.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto extends AbstractDto implements Serializable {

    @NotEmpty
    @Size(min = 3, max = 20)
    private String username;

    @NotEmpty
    @Size(min = 6, max = 20)
    private String password;
}
