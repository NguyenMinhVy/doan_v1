package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.entity.Lecturer;
import doan.doan_v1.entity.User;
import doan.doan_v1.repository.LecturerRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LecturerService;
import doan.doan_v1.dto.LecturerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LecturerServiceImpl implements LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        lecturer.setLectureCode(lecturerDto.getLecturerCode());
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
}