package doan.doan_v1.controller;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Incident;
import doan.doan_v1.mapper.IncidentMapper;
import doan.doan_v1.repository.IncidentRepository;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/device")
public class DeviceController {

    @Autowired
    private UserService userService;

    @Autowired
    private IncidentMapper incidentMapper;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private DeviceService deviceService;

    @PostMapping("/incident/add")
    public String addIncident(@ModelAttribute IncidentDto incidentDto, RedirectAttributes redirectAttributes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto userDto = userService.findByUsername(username);
        incidentDto.setReportUser(userDto.getId());
        Incident incident = incidentMapper.incidentDtoToIncident(incidentDto);
        incident.setInsertId(userDto.getId());
        incident.setUpdateId(userDto.getId());
        incident.setUpdateDate(LocalDateTime.now());
        incident.setInsertDate(LocalDateTime.now());
        incident.setStatus(Constant.STATUS.ERROR);
        incidentRepository.save(incident);

        DeviceDto deviceDto = deviceService.findDeviceDtoById(incidentDto.getComputerDeviceId());
        deviceDto.setStatus(incident.getStatus());
        deviceService.updateDeviceDto(deviceDto);
        // Thêm thông báo thành công
        redirectAttributes.addFlashAttribute("message", "Thêm sự cố thành công!");

        // Điều hướng trở lại trang chi tiết thiết bị
        return "redirect:/incident/device/" + incidentDto.getComputerDeviceId();
    }


}
