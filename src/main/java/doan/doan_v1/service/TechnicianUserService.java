package doan.doan_v1.service;

import doan.doan_v1.dto.UserDto;

public interface TechnicianUserService {
    String createUserForTechnician(UserDto userDto);
    UserDto getUserByUsername(String username);
    UserDto getUserById(Integer id);
} 