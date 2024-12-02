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
import java.util.Arrays;
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
            User user = getRoleCurrentUser();
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

    @GetMapping("/update/{id}")
    public String showUpdateIncidentForm(@PathVariable Integer id, Model model) {
        IncidentDto incident = incidentService.getIncidentDtoById(id);
        if (incident == null) {
            return "redirect:/incident/list";
        }

        // Kiểm tra role của user hiện tại
        User currentUser = getRoleCurrentUser();

        boolean isAdmin = false;
        boolean isTechnician = false;
        if (currentUser.getRoleId() == Constant.ROLE_ID.ROLE_ADMIN){
            isAdmin = true;
        }

        if (currentUser.getRoleId() == Constant.ROLE_ID.ROLE_TECHNICIAN){
            isTechnician = true;
        }
        
        if (!isAdmin && !isTechnician) {
            return "redirect:/incident/list";
        }

        if (isAdmin) {
            // Nếu là admin, thêm danh sách kỹ thuật viên để có thể chọn
            List<TechnicianDto> technicianList = technicianService.getAllTechnicianDto();
            model.addAttribute("technicianList", technicianList);
        }

        // Thêm các lý do chưa xử lý
        List<String> unprocessedReasons = Arrays.asList(
            "Chờ thiết bị mới",
            "Việc cá nhân",
            "Khác"
        );
        
//        // Kiểm tra nếu kỹ thuật viên hiện tại có quyền xem incident này
//        if (isTechnician) {
//            Technician technician = technicianService.findByUserId(currentUser.getId());
//            if (technician == null || !technician.getId().equals(incident.getTechnicianDto().getId())) {
//                return "redirect:/incident/list";
//            }
//        }

        model.addAttribute("incident", incident);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isTechnician", isTechnician);
        model.addAttribute("unprocessedReasons", unprocessedReasons);
        model.addAttribute("isOverdue", incident.getExpectCompleteDate().isBefore(LocalDateTime.now()));
        
        return "updateIncident";
    }

    private User getRoleCurrentUser() {
        return userService.getCurrentUserInfo();
    }

    @PostMapping("/update/{id}")
    public String updateIncident(@PathVariable Integer id, 
                               @ModelAttribute IncidentDto incidentDto) {
        User currentUser = getRoleCurrentUser();
        boolean isAdmin = false;
        boolean isTechnician = false;
        if (currentUser.getRoleId() == Constant.ROLE_ID.ROLE_ADMIN){
            isAdmin = true;
        }

        if (currentUser.getRoleId() == Constant.ROLE_ID.ROLE_TECHNICIAN){
            isTechnician = true;
        }
        
        if (!isAdmin && !isTechnician) {
            return "redirect:/incident/list";
        }

        // Lấy incident hiện tại
        IncidentDto currentIncident = incidentService.getIncidentDtoById(id);
        if (currentIncident == null) {
            return "redirect:/incident/list";
        }

        // Kiểm tra nếu quá hạn
        boolean isOverdue = currentIncident.getExpectCompleteDate().isBefore(LocalDateTime.now());
        if (isOverdue && incidentDto.getStatus() != 5) { // 5 là trạng thái "Đã hoàn thành nhưng quá hạn"
            return "redirect:/incident/update/" + id + "?error=overdue";
        }

        // Nếu là technician, chỉ cho phép update status và lý do
        if (isTechnician) {
            incidentDto.setTechnicianDto(currentIncident.getTechnicianDto());
        }

        try {
            incidentService.updateIncident(id, incidentDto);
            return "redirect:/incident/list?success=true";
        } catch (Exception e) {
            return "redirect:/incident/update/" + id + "?error=true";
        }
    }

    @PostMapping("/incident/update/{id}")
    public String updateIncident(@PathVariable Integer id,
                               @ModelAttribute IncidentDto incidentDto,
                               @RequestParam(value = "shouldUpdateCompletedDate", required = false) boolean shouldUpdateCompletedDate) {
        
        if (shouldUpdateCompletedDate) {
            // Cập nhật thời gian hoàn thành là thời điểm hiện tại
            incidentDto.setCompletedDate(LocalDateTime.now());
        }
        
        // Gọi service để cập nhật
        incidentService.updateIncident(id, incidentDto);
        
        return "redirect:/incident/list";
    }

}
