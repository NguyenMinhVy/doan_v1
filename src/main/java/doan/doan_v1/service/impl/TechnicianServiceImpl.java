package doan.doan_v1.service.impl;

import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.TechnicianMapper;
import doan.doan_v1.mapper.UserMapper;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
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
            technicianDto.setUserDto(userMapper.userToUserDto(user));
            technicianDtoList.add(technicianDto);
        }
        return technicianDtoList;
    }

    @Override
    public TechnicianDto getTechnicianDtoById(int id) {
        Technician technician = technicianRepository.findByIdAndDelFlagFalse(id);
        TechnicianDto technicianDto = technicianMapper.technicianToTechnicianDto(technician);
        User user = userRepository.findById(technician.getUserId()).orElseThrow();
        technicianDto.setUserDto(userMapper.userToUserDto(user));
        return technicianDto;
    }
}
