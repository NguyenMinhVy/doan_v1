package doan.doan_v1.controller;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.TechnicianStatisticsDto;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getStatisticsList(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) List<Integer> technicianIds,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Model model) {

        // Nếu không có ngày đ�ợc chọn, mặc định là ngày hôm nay
        if (startDate == null && endDate == null) {
            startDate = LocalDate.now();
            endDate = LocalDate.now();
        }

        // Lấy danh sách kỹ thuật viên
        List<TechnicianDto> technicianDtoList = (locationId != null)
                ? technicianService.getTechnicianDtoListByLocationId(locationId)
                : technicianService.getAllTechnicianDto();

        // Lấy danh sách sự cố
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoList();

        // Luôn áp dụng filter theo thoi gian vì đã có giá trị mặc định
        LocalDate finalStartDate = startDate;
        LocalDate finalEndDate = endDate;
        incidentDtoList = incidentDtoList.stream()
                .filter(incident -> {
                    LocalDate incidentDate = incident.getReportDate().toLocalDate();
                    return !incidentDate.isBefore(finalStartDate) && !incidentDate.isAfter(finalEndDate);
                })
                .toList();

        // Danh sách kết quả thống kê
        List<TechnicianStatisticsDto> statisticsList = new ArrayList<>();

        for (TechnicianDto technicianDto : technicianDtoList) {
            // Áp dụng bộ lọc theo tên kỹ thuật viên nếu có
            if (technicianIds != null && !technicianIds.isEmpty() 
                && !technicianIds.contains(technicianDto.getId())) {
                continue;
            }

            // Tổng số sự cố
            int totalIncidents = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId()))
                    .count();

            // Số sự cố theo trạng thái
            int unprocessed = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId())
                            && incident.getStatus() == Constant.INCIDENT_STATUS.UNPROCESSED)
                    .count();

            int processing = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId())
                            && incident.getStatus() == Constant.INCIDENT_STATUS.PROCESSING)
                    .count();

            int processed = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId())
                            && incident.getStatus() == Constant.INCIDENT_STATUS.PROCESSED)
                    .count();

            int overdueProcessed = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId())
                            && incident.getStatus() == Constant.INCIDENT_STATUS.OVERDUE_PROCESSED)
                    .count();

            int overdueUnprocessed = (int) incidentDtoList.stream()
                    .filter(incident -> incident.getTechnicianDto() != null
                            && incident.getTechnicianDto().getId() == (technicianDto.getId())
                            && incident.getStatus() == Constant.INCIDENT_STATUS.OVERDUE_UNPROCESSED)
                    .count();

            // Thêm vào danh sách thống kê
            statisticsList.add(new TechnicianStatisticsDto(
                    technicianDto.getUserDto().getName(),
                    totalIncidents,
                    unprocessed,
                    processing,
                    processed,
                    overdueUnprocessed,
                    overdueProcessed
            ));
        }
        List<LocationDto> locations = locationService.getAllLocationsSortedByName();
        model.addAttribute("locations", locations);
        model.addAttribute("technicianList", technicianDtoList);

        // Gửi dữ liệu thống kê qua model
        model.addAttribute("statisticsList", statisticsList);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedTechnicianIds", technicianIds);
        model.addAttribute("locationId", locationId);

        return "statisticsList";
    }
}
