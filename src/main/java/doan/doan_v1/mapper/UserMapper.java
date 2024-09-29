package doan.doan_v1.mapper;

import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userToUserDto(UserDto userDto);
}
