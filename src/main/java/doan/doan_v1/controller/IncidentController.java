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

    @Autowired
    private ComputerDeviceService computerDeviceService;

    @Autowired
    private ComputerSoftwareService computerSoftwareService;

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
            @RequestParam(required = false, defaultValue = "false") boolean filtered,
            Model model) {
        
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoList();
        User currentUser = getRoleCurrentUser();
        boolean isAdmin = currentUser.getRoleId() == Constant.ROLE_ID.ROLE_ADMIN;

        // Lấy thông tin kỹ thuật viên hiện tại nếu không phải admin
        TechnicianDto currentTechnician = null;
        if (!isAdmin) {
            currentTechnician = technicianService.getTechnicianDtoByUserId(currentUser.getId());
        }

        // Kiểm tra và cập nhật trạng thái cho các incident quá hạn
        LocalDateTime now = LocalDateTime.now();
        incidentDtoList.forEach(incident -> {
            if ((incident.getStatus() == Constant.INCIDENT_STATUS.UNPROCESSED)
                && incident.getExpectCompleteDate() != null 
                && incident.getExpectCompleteDate().isBefore(now)) {
                incident.setStatus(Constant.INCIDENT_STATUS.OVERDUE_UNPROCESSED);
                incidentService.updateIncident(incident.getId(), incident);
            }
        });
        
        // Nếu form chưa được submit (filtered = false), áp dụng filter mặc định
        if (!filtered) {
            // Filter mặc định: Chưa xử lý (1) và Đang xử lý (2)
            incidentDtoList = incidentDtoList.stream()
                    .filter(incident -> incident.getStatus() == Constant.INCIDENT_STATUS.UNPROCESSED || incident.getStatus() == Constant.INCIDENT_STATUS.OVERDUE_UNPROCESSED)
                    .collect(Collectors.toList());

            if (!isAdmin) {
                // Nếu là kỹ thuật viên, chỉ hiện sự cố được gán cho họ
                final Integer finalTechnicianId = currentTechnician.getId();
                incidentDtoList = incidentDtoList.stream()
                        .filter(incident -> incident.getTechnicianId() == finalTechnicianId)
                        .collect(Collectors.toList());
            }
        } else {
            // Xử lý các filter được chọn
            if (locationId != null) {
                incidentDtoList = incidentDtoList.stream()
                        .filter(incident -> incident.getLocationId() == locationId)
                        .collect(Collectors.toList());
            }

            if (startDate != null && endDate != null) {
                incidentDtoList = incidentDtoList.stream()
                        .filter(incident -> {
                            LocalDate reportDate = incident.getReportDate().toLocalDate();
                            return !reportDate.isBefore(startDate) && !reportDate.isAfter(endDate);
                        })
                        .collect(Collectors.toList());
            }

            if (status != null) {
                incidentDtoList = incidentDtoList.stream()
                        .filter(incident -> incident.getStatus() == status)
                        .collect(Collectors.toList());
            }

            if (technicianId != null) {
                incidentDtoList = incidentDtoList.stream()
                        .filter(incident -> incident.getTechnicianId() == technicianId)
                        .collect(Collectors.toList());
            }
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
        model.addAttribute("filtered", filtered);
        model.addAttribute("isAdmin", isAdmin);

        return "incidentList";
    }

    @GetMapping("/computer/add")
    public String showAddIncidentForm(@RequestParam Integer computerId, Model model) {
        ComputerDto computerDto = computerService.getComputerById(computerId);
        RoomDto roomDto = roomService.getRoomById(computerDto.getRoomId());
        LocationDto locationDto = roomDto.getLocationDto();
        
        // Lấy danh sách thiết bị và phần mềm có thể được báo cáo sự cố
        List<ComputerDeviceDto> availableDevices = computerDeviceService
            .getDevicesByComputerId(computerId)
            .stream()
            .filter(device -> {
                IncidentDto latestIncident = incidentService.getLatestIncidentByComputerDeviceId(device.getId());
                return latestIncident == null || 
                       latestIncident.getStatus() == 3 || 
                       latestIncident.getStatus() == 5;
            })
            .collect(Collectors.toList());

        List<ComputerSoftwareDto> availableSoftware = computerSoftwareService
            .getSoftwareByComputerId(computerId)
            .stream()
            .filter(software -> {
                IncidentDto latestIncident = incidentService.getLatestIncidentByComputerSoftwareId(software.getId());
                return latestIncident == null || 
                       latestIncident.getStatus() == 3 || 
                       latestIncident.getStatus() == 5;
            })
            .collect(Collectors.toList());

        TechnicianDto technicianDto = technicianService.getTechnicianDtoListByLocationId(locationDto.getId()).get(0);
        List<TechnicianDto> technicianDtoList = technicianService.getAllTechnicianDto();
        IncidentDto incidentDto = new IncidentDto();
        incidentDto.setComputerId(computerId);
        incidentDto.setLocationId(locationDto.getId());
        incidentDto.setTechnicianDto(technicianService.getTechnicianDtoById(incidentDto.getTechnicianId()));

        model.addAttribute("technicianDto", technicianDto);
        model.addAttribute("incidentDto", incidentDto);
        model.addAttribute("technicianDtoList", technicianDtoList);
        model.addAttribute("availableDevices", availableDevices);
        model.addAttribute("availableSoftware", availableSoftware);
        // ... existing code ...
        return "addIncidentForm";
    }

    @PostMapping("/computer/add")
    public String addIncidentForComputer(@ModelAttribute IncidentDto incidentDto, 
                                       @RequestParam(required = false) Integer computerDeviceId,
                                       @RequestParam(required = false) Integer computerSoftwareId,
                                       RedirectAttributes redirectAttributes) {
        try {
            // Validate that either computerDeviceId or computerSoftwareId is provided
            if (computerDeviceId == null && computerSoftwareId == null) {
                throw new IllegalArgumentException("Phải chọn thiết bị hoặc phần mềm bị lỗi");
            }

            incidentDto.setComputerDeviceId(computerDeviceId);
            incidentDto.setComputerSoftwareId(computerSoftwareId);
            incidentDto.setStatus(Constant.INCIDENT_STATUS.UNPROCESSED);
            User user = getRoleCurrentUser();
            incidentDto.setReportUser(user.getId());
            incidentDto.setReportUserName(user.getName());
            incidentDto.setReportDate(LocalDateTime.now());
//            if (incidentDto.getTechnicianDto() == null) {
            ComputerDto computerDto = computerService.getComputerById(incidentDto.getComputerId());
            RoomDto roomDto = roomService.getRoomById(computerDto.getRoomId());
            LocationDto locationDto = roomDto.getLocationDto();
//            TechnicianDto technicianDto = technicianService.getTechnicianDtoListByLocationId(locationDto.getId()).get(0);
//            incidentDto.setTechnicianDto(technicianDto);
////            }
            IncidentDto createdIncidentDto = incidentService.addIncidentForComputer(incidentDto);

            redirectAttributes.addFlashAttribute("message", "Thêm phòng thành công!");
            
            return "redirect:/computer/" + createdIncidentDto.getComputerId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/incident/computer/add?computerId=" + incidentDto.getComputerId();
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

        Integer currentTechnicianId = null;
        if (isTechnician) {
            TechnicianDto currentTechnician = technicianService.getTechnicianDtoByUserId(currentUser.getId());
            if (currentTechnician != null) {
                currentTechnicianId = currentTechnician.getId();
            }
        }

        model.addAttribute("currentTechnicianId", currentTechnicianId);
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
                           @ModelAttribute IncidentDto incidentDto,
                           RedirectAttributes redirectAttributes) {
        // Lấy incident hiện tại
        IncidentDto currentIncident = incidentService.getIncidentDtoById(id);
        if (currentIncident == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy sự cố");
            return "redirect:/incident/list";
        }

        // Kiểm tra quyền truy cập
        User currentUser = getRoleCurrentUser();
        boolean isAdmin = currentUser.getRoleId() == Constant.ROLE_ID.ROLE_ADMIN;
        boolean isTechnician = currentUser.getRoleId() == Constant.ROLE_ID.ROLE_TECHNICIAN;
        
        if (!isAdmin && !isTechnician) {
            return "redirect:/incident/list";
        }

        // Kiểm tra thời hạn
        boolean isOverdue = currentIncident.getExpectCompleteDate().isBefore(LocalDateTime.now());
        
        // Validate trạng thái
        if (currentIncident.getStatus() != incidentDto.getStatus()) {
            if (isOverdue) {
                if (currentIncident.getStatus() == 4 && incidentDto.getStatus() != 2) {
                    redirectAttributes.addFlashAttribute("error", "Chỉ có thể chuyển sang trạng thái Đang xử lý");
                    return "redirect:/incident/update/" + id;
                }
                if (currentIncident.getStatus() == 2 && incidentDto.getStatus() != 5) {
                    redirectAttributes.addFlashAttribute("error", "Chỉ có thể chuyển sang trạng thái Đã hoàn thành nhưng quá hạn");
                    return "redirect:/incident/update/" + id;
                }
            } else {
                if (currentIncident.getStatus() == Constant.INCIDENT_STATUS.UNPROCESSED && incidentDto.getStatus() != 2) {
                    redirectAttributes.addFlashAttribute("error", "Từ trạng thái Chưa xử lý chỉ có thể chuyển sang Đang xử lý");
                    return "redirect:/incident/update/" + id;
                }
                if (currentIncident.getStatus() == 2 && incidentDto.getStatus() != 3) {
                    redirectAttributes.addFlashAttribute("error", "Từ trạng thái Đang xử lý chỉ có thể chuyển sang Đã hoàn thành");
                    return "redirect:/incident/update/" + id;
                }
            }
        }

        try {
            // Cập nhật thời gian hoàn thành nếu cần
            if (incidentDto.getStatus() == 3 || incidentDto.getStatus() == 5) {
                incidentDto.setCompletedDate(LocalDateTime.now());
            }
            if (incidentDto.getTechnicianDto() == null) {
                incidentDto.setTechnicianId(currentIncident.getTechnicianId());
            } else {
                incidentDto.setTechnicianId(incidentDto.getTechnicianDto().getId());
            }

            incidentService.updateIncident(id, incidentDto);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công");
            return "redirect:/incident/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật");
            return "redirect:/incident/update/" + id;
        }
    }

    @PostMapping("/incident/update/{id}")
    public String updateIncident(@PathVariable Integer id,
                               @ModelAttribute IncidentDto incidentDto,
                               @RequestParam(value = "shouldUpdateCompletedDate", required = false) boolean shouldUpdateCompletedDate) {
        
        if (shouldUpdateCompletedDate) {
            // Cập nhật th���i gian hoàn thành là thời điểm hiện tại
            incidentDto.setCompletedDate(LocalDateTime.now());
        }
        
        // Gọi service để cập nhật
        incidentService.updateIncident(id, incidentDto);
        
        return "redirect:/incident/list";
    }

}
