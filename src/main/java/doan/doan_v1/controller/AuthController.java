package doan.doan_v1.controller;

import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.UserDto;
import doan.doan_v1.dto.UserLoginDto;
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

import java.util.List;


@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomService roomService;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("userLoginDto", new UserLoginDto());
        return "login";
    }

    @PostMapping(value = "/auth")
    public String auth(@Valid @ModelAttribute UserLoginDto userLoginDto, BindingResult bindingResult, Model model){
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
    public String home(Model model){
        List<RoomDto> roomList = roomService.getAllRoomList();
        model.addAttribute("roomList", roomList);
        return "home";
    }

}
