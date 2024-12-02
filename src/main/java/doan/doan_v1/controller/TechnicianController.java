package doan.doan_v1.controller;

import doan.doan_v1.dto.*;
import doan.doan_v1.entity.Room;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.entity.User;
import doan.doan_v1.mapper.TechnicianMapper;
import doan.doan_v1.repository.TechnicianRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import doan.doan_v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    private UserRepository userRepository;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority     ('ADMIN')")
    public String showCreateForm(Model model) {
        model.addAttribute("locations", locationService.getAllLocationsSortedByName());
        return "createTechnician";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createTechnician(@RequestParam String fullName,
                                   @RequestParam String technicianCode,
                                   @RequestParam Integer locationId,
                                   Model model) {
        try {
            TechnicianDto technicianDto = new TechnicianDto();
            technicianDto.setTechnicianCode(technicianCode);

            UserDto userDto = new UserDto();
            userDto.setName(fullName);
            technicianDto.setUserDto(userDto);

            LocationDto locationDto = new LocationDto();
            locationDto.setId(locationId);
            technicianDto.setLocationDto(locationDto);

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

    @PostMapping("/delete/{technicianId}")
    public String deleteRoom (@PathVariable("technicianId") int technicianId) {
        TechnicianDto technicianDto = technicianService.getTechnicianDtoById(technicianId);
        Technician technician = technicianMapper.technicianDtoToTechnician(technicianDto);
        technician.setDelFlag(true);
        technicianRepository.save(technician);
        return "redirect:/technician/list";
    }

    @GetMapping("/updateTechnicianForm/{technicianId}")
    public String getUpdateTechnicianForm (@PathVariable("technicianId") int technicianId, Model model) {
        TechnicianDto technicianDto = technicianService.getTechnicianDtoById(technicianId);
        List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
        model.addAttribute("locationDtoList", locationDtoList);
        model.addAttribute("technicianDto", technicianDto);
        return "updateTechnicianForm";
    }

    @PostMapping("/update")
    public String updateTechnician (@ModelAttribute TechnicianDto technicianDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            TechnicianDto oldTechnicianDto = technicianService.getTechnicianDtoById(technicianDto.getId());
            UserDto oldUserDto = oldTechnicianDto.getUserDto();
            if (!oldUserDto.getName().equals(technicianDto.getUserDto().getName())) {
                if (userRepository.existsByUsername(oldUserDto.getName())) {
                    bindingResult.rejectValue("name", "error.name", "Tên đã tồn tại");
//                } else {
//                    String username = technicianService.ge()
//                }
                }
                // Nếu có lỗi, trả về form `createRoomForm` và hiển thị lỗi
                if (bindingResult.hasErrors()) {
                    List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();

                    model.addAttribute("locationDtoList", locationDtoList);
                    return "updateTechnicianForm";
                }
                TechnicianDto updatedDto = technicianService.updateTechnician(technicianDto);

                redirectAttributes.addFlashAttribute("message", "Cp nhật thành công!");
                return "redirect:/technician/list";

            }
            return "redirect:/technician/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật");
            return "redirect:/technician/updateTechnicianForm?error=true&technicianId=" + technicianDto.getId();
        }
    }
}