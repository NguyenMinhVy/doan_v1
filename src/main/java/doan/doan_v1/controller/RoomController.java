package doan.doan_v1.controller;

import doan.doan_v1.dto.*;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.RoomMapper;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.*;
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
    private RoomMapper roomMapper;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TechnicianService technicianService;


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

    @GetMapping("/getTechnicianListByLocationId")
    @ResponseBody
    public List<TechnicianDto> getTechnicianListByLocationId (@RequestParam("locationId") int locationId) {
        return technicianService.getTechnicianDtoListByLocationId(locationId);
    }

    @PostMapping("/delete/{roomId}")
    public String deleteRoom (@PathVariable("roomId") int roomId) {
        RoomDto roomDto = roomService.getRoomById(roomId);
        Room room = roomMapper.roomDtoToRoom(roomDto);
        room.setDelFlag(true);
        roomRepository.save(room);
        return "redirect:/home";
    }

    @GetMapping("/updateRoomForm/{roomId}")
    public String getUpdateRoomForm (@PathVariable("roomId") int roomId, Model model) {
        RoomDto roomDto = roomService.getRoomById(roomId);
        List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
        List<RoomTypeDto> roomTypeDtoList = roomTypeService.getAllRoomTypesSortedByName();
        List<TechnicianDto> technicianDtoList = technicianService.getTechnicianDtoListByLocationId(roomDto.getLocationDto().getId());

        model.addAttribute("roomDto", roomDto);
        model.addAttribute("locationDtoList", locationDtoList);
        model.addAttribute("roomTypeDtoList", roomTypeDtoList);
        model.addAttribute("technicianDtoList", technicianDtoList);
        return "updateRoomForm";
    }

    @PostMapping("/update")
    public String updateRoom (@ModelAttribute RoomDto roomDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            RoomDto oldRoomDto = roomService.getRoomById(roomDto.getId());
            String oldRoomName = oldRoomDto.getName();
            if (!oldRoomName.equals(roomDto.getName())) {
                if (roomService.isRoomNameExist(roomDto.getName())) {
                    bindingResult.rejectValue("name", "error.name", "Tên phòng đã tồn tại");
                }
            }
            // Nếu có lỗi, trả về form `createRoomForm` và hiển thị lỗi
            if (bindingResult.hasErrors()) {
                List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
                List<RoomTypeDto> roomTypeDtoList = roomTypeService.getAllRoomTypesSortedByName();

                model.addAttribute("locationDtoList", locationDtoList);
                model.addAttribute("roomTypeDtoList", roomTypeDtoList);
                return "updateRoomForm";
            }
            if (roomDto.getLocationDto() == null) {
                roomDto.setLocationDto(oldRoomDto.getLocationDto());
            }
            if (roomDto.getRoomTypeDto() == null) {
                roomDto.setRoomTypeDto(oldRoomDto.getRoomTypeDto());
            }
            RoomDto updatedRoom = roomService.updateRoom(roomDto);

            redirectAttributes.addFlashAttribute("message", "Thêm phòng thành công!");
            return "redirect:/room/" + updatedRoom.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm phòng");
            return "redirect:/room/updateRoomForm?error=true&roomId=" + roomDto.getId();
        }

    }


}
