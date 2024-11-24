package doan.doan_v1.controller;

import doan.doan_v1.dto.LecturerDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showCreateForm() {
        return "createLecturer";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createLecturer(@RequestParam String fullName,
                                 @RequestParam String lecturerCode,
                                 Model model) {
        try {
            LecturerDto lecturerDto = new LecturerDto();
            lecturerDto.setLecturerCode(lecturerCode);

            UserDto userDto = new UserDto();
            userDto.setName(fullName);
            lecturerDto.setUserDto(userDto);

            String username = lecturerService.createLecturer(lecturerDto);
            model.addAttribute("username", username);
            return "lecturerSuccess";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "createLecturer";
        }
    }
}