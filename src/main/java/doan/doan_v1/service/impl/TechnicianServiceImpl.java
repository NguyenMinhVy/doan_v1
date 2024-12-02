package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Room;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.TechnicianMapper;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.LocationRepository;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TechnicianServiceImpl implements TechnicianService {

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private TechnicianMapper technicianMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    @Override
    public String createTechnician(TechnicianDto technicianDto) {
        // Kiểm tra mã kỹ thuật viên đã tồn tại chưa
        if (technicianRepository.existsByTechnicianCodeAndDelFlagFalse(technicianDto.getTechnicianCode())) {
            throw new RuntimeException("Mã kỹ thuật viên đã tồn tại");
        }

        // Tạo username từ họ tên
        String username = generateUsername(technicianDto.getUserDto().getName());

        // Tạo mới User
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRoleId(Constant.ROLE_ID.ROLE_TECHNICIAN);
        user.setName(technicianDto.getUserDto().getName());
        userRepository.save(user);

        // Tạo mới Technician
        Technician technician = new Technician();
        technician.setTechnicianCode(technicianDto.getTechnicianCode());
        technician.setUserId(user.getId());
        technician.setLocationId(locationRepository.findByIdAndDelFlagFalse(technicianDto.getLocationDto().getId()).getId());
        technician.setDelFlag(false);

        technicianRepository.save(technician);

        return username;
    }

    public String generateUsername(String fullName) {
        // Tách họ tên thành các phần
        String[] parts = fullName.toLowerCase()
                .replaceAll("đ", "d")
                .replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .split("\\s+");

        // Lấy tên và họ
        String firstName = parts[parts.length - 1];
        String lastName = parts[0];

        // Tạo username cơ bản
        String baseUsername = firstName + "." + lastName;

        // Kiểm tra và thêm số nếu username đã tồn tại
        String username = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }


    @Override
    public List<TechnicianDto> getTechnicianDtoListByLocationId(int locationId) {
        List<Technician> technicianList = technicianRepository.findByLocationId(locationId);
        List<TechnicianDto> technicianDtoList = new ArrayList<>();
        for (Technician technician : technicianList) {
            TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDto(technician);
            User user = userRepository.findById(technician.getUserId()).orElseThrow();
            technicianDto.setUserDto(userMapper.userToUserDto(user));
            technicianDtoList.add(technicianDto);
        }
        return technicianDtoList;
    }

    @Override
    public List<TechnicianDto> getAllTechnicianDto() {
        List<Technician> technicianList = technicianRepository.findByDelFlagFalse();
        List<TechnicianDto> technicianDtoList = new ArrayList<>();
        for (Technician technician : technicianList) {
            TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDto(technician);
            User user = userRepository.findById(technician.getUserId()).orElseThrow();
            LocationDto locationDto = locationService.getLocationById(technician.getLocationId());
            technicianDto.setUserDto(userMapper.userToUserDto(user));
            technicianDto.setLocationDto(locationDto);
            technicianDtoList.add(technicianDto);
        }
        return technicianDtoList;
    }

    @Override
    public TechnicianDto getTechnicianDtoById(int id) {
        Technician technician = technicianRepository.findByIdAndDelFlagFalse(id);
        if (technician == null) {
            return null;
        }
        TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDto(technician);
        LocationDto locationDto =locationService.getLocationById(technician.getLocationId());
        User user = userRepository.findById(technician.getUserId()).orElseThrow();
        technicianDto.setUserDto(userMapper.userToUserDto(user));
        technicianDto.setLocationDto(locationDto);
        return technicianDto;
    }

    @Override
    public TechnicianDto getTechnicianDtoByUserId(int userId) {
        Technician technician = technicianRepository.findByUserIdAndDelFlagFalse(userId);
        if (technician == null) {
            return null;
        }
        TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDto(technician);
        LocationDto locationDto =locationService.getLocationById(technician.getLocationId());
        technicianDto.setLocationDto(locationDto);
        User user = userRepository.findById(technician.getUserId()).orElseThrow();
        technicianDto.setUserDto(userMapper.userToUserDto(user));
        return technicianDto;
    }

    @Override
    public TechnicianDto updateTechnician(TechnicianDto technicianDto) {
        Technician technician = technicianMapper.technicianDtoToTechnician(technicianDto);
        technician.setLocationId(technicianDto.getLocationDto().getId());
        technician.setUserId(technicianDto.getUserDto().getId());
        technicianRepository.save(technician);
        return technicianDto;
    }
}
