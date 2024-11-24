package doan.doan_v1.controller;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.TechnicianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/technician")
public class TechnicianController {

    @Autowired
    private TechnicianService technicianService;

    @Autowired
    private LocationService locationService;

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
}