package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.TechnicianLocation;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.TechnicianMapper;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.LocationRepository;
import doan.doan_v1.repository.TechnicianLocationRepository;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import doan.doan_v1.service.UserService;
import doan.doan_v1.service.TechnicianUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TechnicianServiceImpl implements TechnicianService {

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    private TechnicianLocationRepository technicianLocationRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private TechnicianMapper technicianMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TechnicianUserService technicianUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public TechnicianDto getTechnicianDtoById(Integer id) {
        if (id == null || id == 0) {
            return null;
        }
        Technician technician = technicianRepository.findById(id).orElse(null);
        if (technician == null) {
            return null;
        }
        
        List<TechnicianLocation> technicianLocations = technicianLocationRepository.findByTechnicianId(id);
        return technicianMapper.technicianToTechnicianDtoWithLocations(technician, technicianLocations);
    }

    @Override
    public List<TechnicianDto> getAllTechnicianDto() {
        List<Technician> technicians = technicianRepository.findAllByDelFlagFalse();
        return technicians.stream()
            .map(technician -> {
                TechnicianDto dto = technicianMapper.technicianToTechnicianDto(technician);
                
                // Lấy thông tin user
                User user = userRepository.findById(technician.getUserId()).orElse(null);
                if (user != null) {
                    dto.setUserDto(userMapper.userToUserDto(user));
                }
                
                // Lấy danh sách location
                List<TechnicianLocation> technicianLocations =
                    technicianLocationRepository.findByTechnicianId(technician.getId());
                if (!technicianLocations.isEmpty()) {
                    List<LocationDto> locationDtos = technicianLocations.stream()
                        .map(tl -> locationRepository.findById(tl.getLocationId())
                            .map(location -> {
                                LocationDto locationDto = new LocationDto();
                                locationDto.setId(location.getId());
                                locationDto.setName(location.getName());
                                return locationDto;
                            })
                            .orElse(null))
                        .collect(Collectors.toList());
                    dto.setLocationDtos(locationDtos);
                }
                
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createTechnician(TechnicianDto technicianDto) {
        if (technicianRepository.existsByTechnicianCodeAndDelFlagFalse(technicianDto.getTechnicianCode())) {
            throw new RuntimeException("Mã kỹ thuật viên đã tồn tại");
        }

        String username = generateUsername(technicianDto.getUserDto().getName());
        
        User user = new User();
        user.setUsername(username);
        user.setName(technicianDto.getUserDto().getName());
        user.setPassword(passwordEncoder.encode("defaultPassword"));
        user.setRoleId(Constant.ROLE_ID.ROLE_TECHNICIAN);
        User savedUser = userRepository.save(user);

        Technician technician = technicianMapper.technicianDtoToTechnician(technicianDto);
        technician.setUserId(savedUser.getId());
        Technician savedTechnician = technicianRepository.save(technician);

        if (technicianDto.getLocationDtos() != null) {
            for (LocationDto locationDto : technicianDto.getLocationDtos()) {
                TechnicianLocation technicianLocation = new TechnicianLocation();
                technicianLocation.setTechnicianId(savedTechnician.getId());
                technicianLocation.setLocationId(locationDto.getId());
                technicianLocationRepository.save(technicianLocation);
            }
        }

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
    public TechnicianDto getTechnicianDtoByUserId(Integer userId) {
        Technician technician = technicianRepository.findByUserId(userId);
        if (technician == null) {
            return null;
        }
        
        List<TechnicianLocation> technicianLocations = 
            technicianLocationRepository.findByTechnicianId(technician.getId());
        return technicianMapper.technicianToTechnicianDtoWithLocations(technician, technicianLocations);
    }

    @Override
    public List<TechnicianDto> getTechnicianDtoListByLocationId(Integer locationId) {
        List<TechnicianLocation> technicianLocations = technicianLocationRepository.findByLocationId(locationId);
        
        List<Integer> technicianIds = technicianLocations.stream()
            .map(TechnicianLocation::getTechnicianId)
            .collect(Collectors.toList());
        
        List<Technician> technicians = technicianRepository.findAllById(technicianIds);
        
        return technicians.stream()
            .map(technician -> {
                List<TechnicianLocation> techLocations = 
                    technicianLocationRepository.findByTechnicianId(technician.getId());
                TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDtoWithLocations(technician, techLocations);
                User user = userRepository.findById(technician.getUserId()).orElse(null);
                UserDto userDto = userMapper.userToUserDto(user);
                technicianDto.setUserDto(userDto);
                return technicianDto;
            })
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateTechnicianLocations(Integer technicianId, List<Integer> locationIds) {
        // Xóa tất cả các location hiện tại của technician
        technicianLocationRepository.deleteByTechnicianId(technicianId);
        
        // Thêm các location mới
        locationIds.forEach(locationId -> {
            TechnicianLocation technicianLocation = new TechnicianLocation();
            technicianLocation.setTechnicianId(technicianId);
            technicianLocation.setLocationId(locationId);
            technicianLocationRepository.save(technicianLocation);
        });
    }

    @Override
    public boolean isTechnicianInLocation(Integer technicianId, Integer locationId) {
        return technicianLocationRepository.existsByTechnicianIdAndLocationId(technicianId, locationId);
    }

    @Override
    public TechnicianDto getTechnicianDtoById(int id) {
        Technician technician = technicianRepository.findByIdAndDelFlagFalse(id);
        if (technician == null) {
            return null;
        }
        
        User user = userRepository.findById(technician.getUserId()).orElse(null);
        
        List<TechnicianLocation> technicianLocations = technicianLocationRepository.findByTechnicianId(id);
        
        TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDtoWithLocations(technician, technicianLocations);
        if (user != null) {
            technicianDto.setUserDto(userMapper.userToUserDto(user));
        }
        
        return technicianDto;
    }

    @Override
    @Transactional
    public void updateTechnician(TechnicianDto technicianDto) {
        // Kiểm tra technician tồn tại
        Technician existingTechnician = technicianRepository.findByIdAndDelFlagFalse(technicianDto.getId());
        if (existingTechnician == null) {
            throw new RuntimeException("Không tìm thấy kỹ thuật viên");
        }

        // Cập nhật thông tin user
        User user = userRepository.findById(existingTechnician.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user"));
        
        user.setName(technicianDto.getUserDto().getName());
        userRepository.save(user);

        // Cập nhật thông tin technician
        existingTechnician.setTechnicianCode(technicianDto.getTechnicianCode());
        technicianRepository.save(existingTechnician);

        // Cập nhật danh sách location
        // Xóa tất cả các location hiện tại
        technicianLocationRepository.deleteByTechnicianId(existingTechnician.getId());

        // Thêm các location mới
        if (technicianDto.getLocationDtos() != null) {
            for (LocationDto locationDto : technicianDto.getLocationDtos()) {
                TechnicianLocation technicianLocation = new TechnicianLocation();
                technicianLocation.setTechnicianId(existingTechnician.getId());
                technicianLocation.setLocationId(locationDto.getId());
                technicianLocationRepository.save(technicianLocation);
            }
        }
    }

    @Override
    @Transactional
    public void deleteTechnician(Integer id) {
        Technician technician = technicianRepository.findByIdAndDelFlagFalse(id);
        if (technician != null) {
            // Soft delete
            technician.setDelFlag(true);
            technicianRepository.save(technician);
            
            // Xóa các liên kết với location
            technicianLocationRepository.deleteByTechnicianId(id);
        }
    }

}
