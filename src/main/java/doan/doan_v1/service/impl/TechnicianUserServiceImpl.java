package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.TechnicianUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TechnicianUserServiceImpl implements TechnicianUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String createUserForTechnician(UserDto userDto) {
        String username = generateUsername(userDto.getName());

        User user = new User();
        user.setUsername(username);
        user.setName(userDto.getName());
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setRoleId(Constant.ROLE_ID.ROLE_TECHNICIAN);

        userRepository.save(user);
        return username;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        return userMapper.userToUserDto(user);
    }

    @Override
    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDto)
                .orElse(null);
    }

    private String generateUsername(String fullName) {
        String username = fullName.toLowerCase().replaceAll("\\s+", "");
        int suffix = 1;
        String originalUsername = username;
        while (userRepository.existsByUsername(username)) {
            username = originalUsername + suffix;
            suffix++;
        }
        return username;
    }
} 