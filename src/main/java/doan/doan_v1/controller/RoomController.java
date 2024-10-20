package doan.doan_v1.controller;

import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private ComputerService computerService;

    @Autowired
    private RoomService roomService;


    @GetMapping(value = "/{roomId}")
    public String getRoomDetail (@PathVariable("roomId") int roomId, Model model) {
        List<ComputerDto> computerDtoList = computerService.getComputerListByRoomId(roomId);
        model.addAttribute("computerDtoList", computerDtoList);
        return "roomDetail";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute RoomDto roomDto) {
        roomService.createRoom(roomDto);
        return "redirect:/";
    }
}
