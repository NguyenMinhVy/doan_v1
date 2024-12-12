package doan.doan_v1.controller;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.mapper.TechnicianMapper;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/technician")
public class TechnicianController {

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private TechnicianMapper technicianMapper;

    @Autowired
    private TechnicianRepository technicianRepository;

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("locations", locationService.getAllLocationsSortedByName());
        return "createTechnician";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String createTechnician(@RequestParam String fullName,
                                 @RequestParam String technicianCode,
                                 @RequestParam List<Integer> locationIds,
                                 Model model) {
        try {
            TechnicianDto technicianDto = new TechnicianDto();
            technicianDto.setTechnicianCode(technicianCode);
            
            UserDto userDto = new UserDto();
            userDto.setName(fullName);
            technicianDto.setUserDto(userDto);
            
            List<LocationDto> locationDtos = locationIds.stream()
                .map(id -> {
                    LocationDto dto = new LocationDto();
                    dto.setId(id);
                    return dto;
                })
                .collect(Collectors.toList());
            technicianDto.setLocationDtos(locationDtos);

            String username = technicianService.createTechnician(technicianDto);
            model.addAttribute("username", username);
            return "technicianSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("locations", locationService.getAllLocationsSortedByName());
            return "createTechnician";
        }
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority     ('ADMIN')")
    public String getTechnicianList(Model model) {
        List<TechnicianDto> technicianDtoList = technicianService.getAllTechnicianDto();
        model.addAttribute("technicianDtoList", technicianDtoList);
        return "technicianList";
    }

//    @PostMapping("/delete/{technicianId}")
//    public String deleteTechnician (@PathVariable("technicianId") int technicianId) {
//        TechnicianDto technicianDto = technicianService.getTechnicianDtoById(technicianId);
//        Technician technician = technicianMapper.technicianDtoToTechnician(technicianDto);
//        technician.setDelFlag(true);
//        technicianRepository.save(technician);
//        return "redirect:/technician/list";
//    }

    @GetMapping("/edit/{technicianId}")
    public String getUpdateTechnicianForm(@PathVariable("technicianId") int technicianId, Model model) {
        TechnicianDto technicianDto = technicianService.getTechnicianDtoById(technicianId);
        List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
        model.addAttribute("locationDtoList", locationDtoList);
        model.addAttribute("technicianDto", technicianDto);
        return "updateTechnicianForm";
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTechnician(@ModelAttribute TechnicianDto technicianDto,
                                 @RequestParam List<Integer> locationIds,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Chuyển locationIds thành locationDtos
            List<LocationDto> locationDtos = locationIds.stream()
                .map(id -> {
                    LocationDto dto = new LocationDto();
                    dto.setId(id);
                    return dto;
                })
                .collect(Collectors.toList());
            technicianDto.setLocationDtos(locationDtos);
            
            technicianService.updateTechnician(technicianDto);
            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
            return "redirect:/technician/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật");
            return "redirect:/technician/updateTechnicianForm?error=true&technicianId=" + technicianDto.getId();
        }
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTechnician(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            technicianService.deleteTechnician(id);
            redirectAttributes.addFlashAttribute("message", "Xóa kỹ thuật viên thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa kỹ thuật viên này!");
        }
        return "redirect:/technician/list";
    }
}