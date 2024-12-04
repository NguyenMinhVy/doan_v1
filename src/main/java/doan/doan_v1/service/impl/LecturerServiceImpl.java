package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Lecturer;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.LecturerMapper;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.LecturerRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LecturerService;
import doan.doan_v1.dto.LecturerDto;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LecturerMapper lecturerMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String createLecturer(LecturerDto lecturerDto) {
        // Kiểm tra mã giảng viên đã tồn tại chưa
        if (lecturerRepository.existsByLecturerCodeAndDelFlagFalse(lecturerDto.getLecturerCode())) {
            throw new RuntimeException("Mã giảng viên đã tồn tại");
        }

        // Tạo username từ họ tên
        String username = generateUsername(lecturerDto.getUserDto().getName());

        // Tạo mới User
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRoleId(Constant.ROLE_ID.ROLE_LECTURE);
        user.setName(lecturerDto.getUserDto().getName());
        userRepository.save(user);

        // Tạo mới Lecturer
        Lecturer lecturer = new Lecturer();
        lecturer.setLecturerCode(lecturerDto.getLecturerCode());
        lecturer.setUserId(user.getId());
        lecturer.setDelFlag(false);

        lecturerRepository.save(lecturer);

        return username;
    }

    private String generateUsername(String fullName) {
        String[] parts = fullName.toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .split("\\s+");

        String firstName = parts[parts.length - 1];
        String lastName = parts[0];

        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    @Override
    public List<LecturerDto> getAllLecturerDto() {
        List<Lecturer> lecturerList = lecturerRepository.findByDelFlagFalse();
        List<LecturerDto> lecturerDtoList = new ArrayList<>();
        for (Lecturer lecturer : lecturerList) {
            LecturerDto lecturerDto = lecturerMapper.lectureToLecturerDto(lecturer);
            User user = userRepository.findById(lecturer.getUserId()).orElse(null);
            if (user != null) {
                UserDto userDto = userMapper.userToUserDto(user);
                lecturerDto.setUserDto(userDto);
            }
            lecturerDtoList.add(lecturerDto);
        }
        return lecturerDtoList;
    }

    @Override
    public LecturerDto getLecturerDtoById(int id) {
        Lecturer lecturer = lecturerRepository.findById(id).orElse(null);
        LecturerDto lecturerDto = new LecturerDto();
        if (lecturer != null) {
            lecturerDto = lecturerMapper.lectureToLecturerDto(lecturer);
            UserDto userDto = userMapper.userToUserDto(userRepository.findById(lecturer.getUserId()).orElse(null));
            lecturerDto.setUserDto(userDto);
        }
        return lecturerDto;
    }

    @Override
    public LecturerDto getLecturerDtoByUserId(int userId) {
        Lecturer lecturer = lecturerRepository.findByUserIdAndDelFlagFalse(userId);
        LecturerDto lecturerDto = lecturerMapper.lectureToLecturerDto(lecturer);
        UserDto userDto = userMapper.userToUserDto(userRepository.findById(userId).orElse(null));
        lecturerDto.setUserDto(userDto);
        return lecturerDto;
    }

    @Override
    public LecturerDto updateLecturerDto(LecturerDto lecturerDto) {
        Lecturer lecturer = lecturerMapper.lectureDtoToLecturer(lecturerDto);
        lecturer.setUserId(lecturerDto.getUserDto().getId());
        User user = userRepository.findById(lecturer.getUserId()).orElse(null);
        if (user != null) {
            user.setName(lecturerDto.getUserDto().getName());
            userRepository.save(user);
        }
        lecturerRepository.save(lecturer);
        return lecturerDto;
    }

}