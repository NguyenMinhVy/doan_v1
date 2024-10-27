package doan.doan_v1.controller;

import doan.doan_v1.dto.*;
import doan.doan_v1.entity.Computer;
import doan.doan_v1.entity.Room;
import doan.doan_v1.mapper.ComputerMapper;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.RoomService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/computer")
public class ComputerController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;

    @Autowired
    private ComputerService computerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ComputerMapper computerMapper;

    @Autowired
    private ComputerRepository computerRepository;

    @GetMapping("/{computerId}")
    public String getComputerDetail(@PathVariable("computerId") int computerId, Model model) {
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoByComputerId(computerId);
        List<SoftWareDto> softWareDtoList = softWareService.getSoftWareDtoListByComputerId(computerId);
        model.addAttribute("softWareDtoList", softWareDtoList);
        model.addAttribute("deviceDtoList", deviceDtoList);
        return "computer";
    }

    @GetMapping("/createComputerForm")
    public String createComputerForm(@RequestParam("roomId") int roomId, Model model) {
        ComputerDto computerDto = new ComputerDto();
        computerDto.setRoomId(roomId);
        model.addAttribute("computerDto", computerDto);
        setComputerModel(model);
        return "createComputerForm";
    }

    private void setComputerModel(Model model) {
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoList();
        List<SoftWareDto> softWareDtoList = softWareService.getAllSoftWareDtoList();
        model.addAttribute("deviceDtos", deviceDtoList);
        model.addAttribute("softWareDtos", softWareDtoList);
    }

    @PostMapping("/create")
    public String createComputer(@ModelAttribute ComputerDto computerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            if (computerService.isComputerNameExist(computerDto.getName())) {
                bindingResult.rejectValue("name", "error.name", "Tên máy đã tồn tại");
            }

            // Nếu có lỗi, trả về form `createRoomForm` và hiển thị lỗi
            if (bindingResult.hasErrors()) {
                setComputerModel(model);
                return "createComputerForm";
            }

            ComputerDto createdComputer = computerService.createComputer(computerDto);

            redirectAttributes.addFlashAttribute("message", "Thêm máy tính thành công!");
            return "redirect:/computer/" + createdComputer.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm máy tính");
            return "redirect:/computer/createComputerForm?error=true&roomId=" + computerDto.getRoomId();
        }
    }

    @PostMapping("/delete/{computerId}")
    public String deleteRoom(@PathVariable("computerId") int computerId) {
        ComputerDto computerDto = computerService.getComputerById(computerId);
        Computer computer = computerMapper.computerDtoToComputer(computerDto);
        computer.setDelFlag(true);
        computerRepository.save(computer);
        return "redirect:/home";
    }

    @GetMapping("/update/{computerId}")
    public String getUpdateComputerForm(@PathVariable("computerId") int computerId, Model model) {
        ComputerDto computerDto = computerService.getComputerById(computerId);
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoList();
        List<SoftWareDto> softWareDtoList = softWareService.getAllSoftWareDtoList();
        model.addAttribute("softWareDtoList", softWareDtoList);
        model.addAttribute("deviceDtoList", deviceDtoList);

        model.addAttribute("computerDto", computerDto);

        return "updateComputer";
    }

    @PostMapping("/update")
    public String updateComputer(@ModelAttribute ComputerDto computerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            ComputerDto oldComputerDto = computerService.getComputerById(computerDto.getId());
            String oldComputerName = oldComputerDto.getName();
            if (!oldComputerName.equals(computerDto.getName())) {
                if (computerService.isComputerNameExist(computerDto.getName())) {
                    bindingResult.rejectValue("name", "error.name", "Tên phòng đã tồn tại");
                }
            }
            // Nếu có lỗi, trả về form `createComputerForm` và hiển thị lỗi
            if (bindingResult.hasErrors()) {
//                List<LocationDto> locationDtoList = locationService.getAllLocationsSortedByName();
//                List<ComputerTypeDto> computerTypeDtoList = computerTypeService.getAllComputerTypesSortedByName();

//                model.addAttribute("locationDtoList", locationDtoList);
//                model.addAttribute("computerTypeDtoList", computerTypeDtoList);
                return "updateComputer";
            }
//            if (computerDto.getLocationDto() == null) {
//                roomDto.setLocationDto(oldComputerDto.getLocationDto());
//            }
//            if (roomDto.getComputerTypeDto() == null) {
//                roomDto.setComputerTypeDto(oldComputerDto.getComputerTypeDto());
//            }
            computerService.updateComputer(computerDto.getId(), computerDto);

            redirectAttributes.addFlashAttribute("message", "Thêm phòng thành công!");
            return "redirect:/computer/" + computerDto.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm phòng");
            return "redirect:/computer/updateComputer?error=true&computerId=" + computerDto.getId();
        }

    }
}

//    // Hiển thị form cập nhật máy tính
//    @GetMapping("/update/{id}")
//    public String showUpdateForm(@PathVariable int id, Model model) {
//        ComputerDto computerDto = computerService.getComputerById(id);
//        List<RoomDto> rooms = roomService.getAllRooms();
//        model.addAttribute("computer", computerDto);
//        model.addAttribute("rooms", rooms);
//        return "updateComputer";
//    }
//
//    @PostMapping("/update/{id}")
//    public String updateComputer(@PathVariable int id, @ModelAttribute ComputerDto computerDto) {
//        computerService.updateComputer(id, computerDto);
//        return "redirect:/room/" + computerDto.getRoomId();
//    }
//
//    // Xử lý xóa máy tính
//    @PostMapping("/delete/{id}")
//    @ResponseBody
//    public ResponseEntity<String> deleteComputer(@PathVariable int id) {
//        computerService.deleteComputer(id);
//        return ResponseEntity.ok("Máy tính đã được xóa thành công");
//    }
//}
