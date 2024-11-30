package doan.doan_v1.controller;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.*;
import doan.doan_v1.entity.User;
import doan.doan_v1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/incident")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private ComputerService computerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/device/{deviceId}/{computerId}")
    public String getIncidentDetailByDeviceId (@PathVariable("deviceId") int deviceId, @PathVariable("computerId") int computerId, Model model) {
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoListByDeviceId(deviceId);
        DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
        model.addAttribute("deviceDto", deviceDto);
        model.addAttribute("incidentDtoList", incidentDtoList);
        return "incident";
    }

    @GetMapping("/software/{softwareId}/{computerId}")
    public String getIncidentDetailBySoftwareId (@PathVariable("softwareId") int softwareId, @PathVariable("computerId") int computerId, Model model) {
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoListBySoftwareId(softwareId);
        SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softwareId);
        model.addAttribute("softWareDto", softWareDto);
        model.addAttribute("incidentDtoList", incidentDtoList);
        return "incident";
    }

    @GetMapping("/add")
    public String addIncident (IncidentDto incidentDtoRequest , Model model) {
        IncidentDto incidentDto = incidentService.addIncident(incidentDtoRequest);
        model.addAttribute("incidentDto", incidentDto);
        return "incident";
    }

    @GetMapping("/list")
    public String getIncidentList(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer technicianId,
            Model model) {
        
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoList();
        
        // Lọc theo locationId
        if (locationId != null) {
            incidentDtoList = incidentDtoList.stream()
                    .filter(incident -> incident.getLocationId() == locationId)
                    .collect(Collectors.toList());
        }
        
        // Lọc theo khoảng thời gian
        if (startDate != null && endDate != null) {
            incidentDtoList = incidentDtoList.stream()
                    .filter(incident -> {
                        LocalDate reportDate = incident.getReportDate().toLocalDate();
                        return !reportDate.isBefore(startDate) && !reportDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        
        // Lọc theo status
        if (status != null) {
            incidentDtoList = incidentDtoList.stream()
                    .filter(incident -> incident.getStatus() == status)
                    .collect(Collectors.toList());
        }
        
        // Lọc theo technicianId
        if (technicianId != null) {
            incidentDtoList = incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto().getId() == technicianId)
                    .collect(Collectors.toList());
        }

        // Sắp xếp giảm dần theo id
        incidentDtoList = incidentDtoList.stream()
                .sorted(Comparator.comparing(IncidentDto::getId).reversed())
                .collect(Collectors.toList());

        // Thêm dữ liệu cho các combobox
        List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
        List<TechnicianDto> technicianDtoList = technicianService.getAllTechnicianDto();

        model.addAttribute("locationDtoList", locationDtoList);
        model.addAttribute("technicianDtoList", technicianDtoList);
        model.addAttribute("incidentDtoList", incidentDtoList);
        
        // Thêm các giá trị đã chọn để giữ lại trên form
        model.addAttribute("selectedLocationId", locationId);
        model.addAttribute("selectedStartDate", startDate);
        model.addAttribute("selectedEndDate", endDate);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedTechnicianId", technicianId);
        
        return "incidentList";
    }

    @GetMapping("/computer/add")
    public String getAddIncidentForComputerForm (@RequestParam("computerId") int computerId, Model model) {
        ComputerDto computerDto = computerService.getComputerById(computerId);
        RoomDto roomDto = roomService.getRoomById(computerDto.getRoomId());
        LocationDto locationDto= roomDto.getLocationDto();
        TechnicianDto technicianDto = technicianService.getTechnicianDtoListByLocationId(locationDto.getId()).get(0);
        List<TechnicianDto> technicianDtoList = technicianService.getAllTechnicianDto();
        IncidentDto incidentDto = new IncidentDto();
        incidentDto.setComputerId(computerId);
        incidentDto.setLocationId(locationDto.getId());
        model.addAttribute("technicianDto", technicianDto);
        model.addAttribute("incidentDto", incidentDto);
        model.addAttribute("technicianDtoList", technicianDtoList);
        return "addIncidentForm";
    }

    @PostMapping("/computer/add")
    public String addIncidentForComputer (@ModelAttribute IncidentDto incidentDto, RedirectAttributes redirectAttributes, Model model) {
        try {
            incidentDto.setStatus(Constant.INCIDENT_STATUS.UNPROCESSED);
            User user = userService.getCurrentUserInfo();
            incidentDto.setReportUser(user.getId());
            incidentDto.setReportUserName(user.getName());
            incidentDto.setReportDate(LocalDateTime.now());
            if (incidentDto.getTechnicianDto() == null) {
                ComputerDto computerDto = computerService.getComputerById(incidentDto.getComputerId());
                RoomDto roomDto = roomService.getRoomById(computerDto.getRoomId());
                LocationDto locationDto= roomDto.getLocationDto();
                TechnicianDto technicianDto = technicianService.getTechnicianDtoListByLocationId(locationDto.getId()).get(0);
                incidentDto.setTechnicianDto(technicianDto);
            }
            IncidentDto createdIncidentDto = incidentService.addIncidentForComputer(incidentDto);
            redirectAttributes.addFlashAttribute("message", "Thêm phòng thành công!");
            return "redirect:/computer/" + createdIncidentDto.getComputerId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm phòng");
            return "redirect:/computer/add?computerId=" + incidentDto.getComputerId() + "&error=true";
        }
    }

}
