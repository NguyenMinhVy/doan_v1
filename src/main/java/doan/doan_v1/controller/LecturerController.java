package doan.doan_v1.controller;

import doan.doan_v1.dto.LecturerDto;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.TechnicianDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.entity.Lecturer;
import doan.doan_v1.entity.Technician;
import doan.doan_v1.mapper.LecturerMapper;
import doan.doan_v1.repository.LecturerRepository;
import doan.doan_v1.repository.UserRepository;
import doan.doan_v1.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private LecturerMapper lecturerMapper;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/list")
    @PreAuthorize("hasAuthority     ('ADMIN')")
    public String getLectureList(Model model) {
        List<LecturerDto> lecturerDtoList = lecturerService.getAllLecturerDto();
        model.addAttribute("lecturerDtoList", lecturerDtoList);
        return "lecturerList";
    }

    @PostMapping("/delete/{lecturerId}")
    public String deleteLecture (@PathVariable("lecturerId") int lecturerId) {
        LecturerDto lecturerDto = lecturerService.getLecturerDtoById(lecturerId);
        Lecturer lecturer = lecturerMapper.lectureDtoToLecturer(lecturerDto);
        lecturer.setDelFlag(true);
        lecturerRepository.save(lecturer);
        return "redirect:/lecturer/list";
    }

    @GetMapping("/updateLecturerForm/{lecturerId}")
    public String getUpdateLecturerForm (@PathVariable("lecturerId") int lecturerId, Model model) {
        LecturerDto lecturerDto = lecturerService.getLecturerDtoById(lecturerId);
        model.addAttribute("lecturerDto", lecturerDto);
        return "updateLecturerForm";
    }

    @PostMapping("/update")
    public String updateLecturer (@ModelAttribute LecturerDto lecturerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            LecturerDto oldLecturerDto = lecturerService.getLecturerDtoById(lecturerDto.getId());
            UserDto oldUserDto = oldLecturerDto.getUserDto();
            if (!oldUserDto.getName().equals(lecturerDto.getUserDto().getName())) {
                if (userRepository.existsByName(lecturerDto.getUserDto().getName())) {
                    bindingResult.rejectValue("userDto.name", "error.name", "Tên đã tồn tại");
                }
            }
            if (bindingResult.hasErrors()) {
                return "updateTechnicianForm";
            }
            LecturerDto updatedDto = lecturerService.updateLecturerDto(lecturerDto);

            redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
            return "redirect:/lecturer/list";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi cập nhật");
            return "redirect:/lecturer/updateLecturerForm?error=true&lecturerId=" + lecturerDto.getId();
        }
    }
}