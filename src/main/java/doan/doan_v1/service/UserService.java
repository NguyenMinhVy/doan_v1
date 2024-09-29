package doan.doan_v1.service;

import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    List<UserDto> findAllUsers();

    UserDto findByUsername(String username);
}
