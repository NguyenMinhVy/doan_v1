package doan.doan_v1.controller;

import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.dto.UserLoginDto;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.RoomService;
import doan.doan_v1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomService roomService;

    @Autowired
    private LocationService locationService;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }

    @PostMapping(value = "/auth")
    public String auth(@Valid @ModelAttribute UserLoginDto userLoginDto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "login";
        }
        UserDto userDto = userService.findByUsername(userLoginDto.getUsername());

        if(userDto == null){
            bindingResult.rejectValue("username", "error.userName", "User not found");
            return "login";
        }

        boolean isValidPassword = passwordEncoder.matches(userLoginDto.getPassword(), userDto.getPassword());
        if(!isValidPassword){
            bindingResult.rejectValue("password", "error.password", "Invalid password");
            return "login";
        }
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        return "redirect:/home";
    }



    @GetMapping("/home")
    public String home(Model model) {
        List<LocationDto> locations = locationService.getAllLocationsSortedByName();
        Map<LocationDto, List<RoomDto>> roomsByLocation = roomService.getRoomsByLocation();

        model.addAttribute("locations", locations);
        model.addAttribute("roomsByLocation", roomsByLocation);
        return "home";
    }

    @Controller
    public class ForgotPasswordController {

        @GetMapping("/forgot-password")
        public String showForgotPasswordForm() {
            return "forgot-password";
        }

        @PostMapping("/forgot-password")
        public String verifyUserInfo(@RequestParam String username, @RequestParam String employeeCode,
                                     @RequestParam String fullName, Model model) {
            boolean isValid = userService.isUser(username, employeeCode, fullName);

            if (isValid) {
                return "redirect:/update-password?username=" + username;
            } else {
                model.addAttribute("error", "Thông tin cung cấp không hợp lệ.");
                return "forgot-password";
            }
        }

        @GetMapping("/update-password")
        public String showUpdatePasswordForm(@RequestParam String username, Model model) {
            model.addAttribute("username", username);
            return "update-password";
        }

        @PostMapping("/update-password")
        public String updatePassword(@RequestParam String username, @RequestParam String newPassword,
                                     @RequestParam String confirmPassword, Model model) {
            if (!newPassword.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu không khớp.");
                return "update-password";
            }

            // Xử lý cập nhật mật khẩu trong cơ sở dữ liệu
            boolean isUpdated = userService.updatePassword(username, newPassword);
            if (isUpdated) {
                model.addAttribute("message", "Cập nhật thành công.");
                return  "redirect:/login;";
            } else {
                model.addAttribute("error", "Cập nhật thất bại.");
                return "update-password";
            }
        }
    }


}
