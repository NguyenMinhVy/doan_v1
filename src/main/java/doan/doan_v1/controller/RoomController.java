package doan.doan_v1.controller;

import doan.doan_v1.dto.ComputerDto;
import doan.doan_v1.dto.LocationDto;
import doan.doan_v1.dto.RoomDto;
import doan.doan_v1.dto.RoomTypeDto;
import doan.doan_v1.entity.Location;
import doan.doan_v1.mapper.LocationMapper;
import doan.doan_v1.mapper.RoomTypeMapper;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.LocationService;
import doan.doan_v1.service.RoomService;
import doan.doan_v1.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private ComputerService computerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private LocationMapper locationMapper;

    @Autowired
    private RoomTypeMapper roomTypeMapper;


    @GetMapping(value = "/{roomId}")
    public String getRoomDetail (@PathVariable("roomId") int roomId, Model model) {
        List<ComputerDto> computerDtoList = computerService.getComputerListByRoomId(roomId);
        model.addAttribute("computerDtoList", computerDtoList);
        return "roomDetail";
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute RoomDto roomDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            if (roomService.isRoomNameExist(roomDto.getName())) {
                bindingResult.rejectValue("name", "error.name", "Tên phòng đã tồn tại");
            }

            // Nếu có lỗi, trả về form `createRoomForm` và hiển thị lỗi
            if (bindingResult.hasErrors()) {
                List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
                List<RoomTypeDto> roomTypeDtoList = roomTypeService.getAllRoomTypesSortedByName();

                model.addAttribute("locationDtoList", locationDtoList);
                model.addAttribute("roomTypeDtoList", roomTypeDtoList);
                return "createRoomForm";
            }

            RoomDto createdRoom = roomService.createRoom(roomDto);

            redirectAttributes.addFlashAttribute("message", "Thêm phòng thành công!");
            return "redirect:/room/" + createdRoom.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm phòng");
            return "redirect:/room/createRoomForm?error=true";
        }
    }

    @GetMapping("/createRoomForm")
    public String createRoomForm (Model model) {
        RoomDto roomDto = new RoomDto();
        List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
        List<RoomTypeDto> roomTypeDtoList = roomTypeService.getAllRoomTypesSortedByName();

        model.addAttribute("roomDto", roomDto);
        model.addAttribute("locationDtoList", locationDtoList);
        model.addAttribute("roomTypeDtoList", roomTypeDtoList);
        return "createRoomForm";
    }

}
