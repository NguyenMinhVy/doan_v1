package doan.doan_v1.service.impl;

import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Role;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.RoleRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");

        if(role==null){
            role=checkRoleExists();
        }
        user.setRoleId(role.getId());
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> findAllUsers() {
        return List.of();
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null){
            return null;
        }
        return userMapper.userToUserDto(user);
    }

    private Role checkRoleExists(){
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    public User getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }

        return new User();
    }
}
