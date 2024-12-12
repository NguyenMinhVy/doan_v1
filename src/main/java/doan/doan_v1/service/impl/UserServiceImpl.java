package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.LecturerDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Role;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.RoleRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LecturerService;
import doan.doan_v1.service.TechnicianService;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    @Lazy
    private TechnicianService technicianService;

    @Autowired
    private LecturerService lecturerService;


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
        if (authentication != null) {
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                Object principal = authentication.getPrincipal();

                if (principal instanceof UserDetails userDetails) {
                    return convertToUser(userDetails);
                }
            }
        }
        return new User();
    }

    @Override
    public boolean isUser(String username, String employeeCode, String fullName) {
        User user = userRepository.findByUsername(username);
        if(user!=null && user.getName().equals(fullName)) {
            int roleId = user.getRoleId();
            boolean isLecturer = roleId == Constant.ROLE_ID.ROLE_LECTURE;
            boolean isTechnician = roleId == Constant.ROLE_ID.ROLE_TECHNICIAN;
            if (isTechnician) {
                TechnicianDto technicianDto = technicianService.getTechnicianDtoByUserId(user.getId());
                if (technicianDto != null) {
                    return technicianDto.getTechnicianCode().equals(employeeCode);
                }
            }
            if (isLecturer) {
                LecturerDto lecturerDto = lecturerService.getLecturerDtoByUserId(user.getId());
                if (lecturerDto != null) {
                    return lecturerDto.getLecturerCode().equals(employeeCode);
                }
            }
        }
        return false;
    }

    @Override
    public boolean updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private User convertToUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername());
    }

//    @Override
//    public UserDto getUserById(Integer id) {
//        return userRepository.findById(id)
//                .map(userMapper::userToUserDto)
//                .orElse(null);
//    }
//
//    @Override
//    public User getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//        return userRepository.findByUsername(authentication.getName());
//    }
}
