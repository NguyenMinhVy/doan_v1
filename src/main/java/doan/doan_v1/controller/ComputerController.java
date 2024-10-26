package doan.doan_v1.controller;

import doan.doan_v1.dto.*;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
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




    @GetMapping("/{computerId}")
    public String getComputerDetail (@PathVariable("computerId") int computerId, Model model) {
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoByComputerId(computerId);
        List<SoftWareDto> softWareDtoList = softWareService.getSoftWareDtoListByComputerId(computerId);
        model.addAttribute("softWareDtoList", softWareDtoList);
        model.addAttribute("deviceDtoList", deviceDtoList);
        return "computer";
    }

    @GetMapping("/createComputerForm")
    public String createComputerForm (@RequestParam("roomId") int roomId, Model model) {
        setComputerModel(roomId, model);
        return "createComputerForm";
    }

    private void setComputerModel(int roomId, Model model) {
        ComputerDto computerDto = new ComputerDto();
        List<DeviceDto> deviceDto = deviceService.findAllDeviceDtoList();
        List<SoftWareDto> softWareDto = softWareService.getAllSoftWareDtoList();
        computerDto.setRoomId(roomId);
        computerDto.setDeviceDtoList(deviceDto);
        computerDto.setSoftWareDtoList(softWareDto);
        model.addAttribute("computerDto", computerDto);
    }

    @PostMapping("/create")
    public String createRoom(@ModelAttribute ComputerDto computerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        try {
            // Kiểm tra nếu tên phòng đã tồn tại
            if (computerService.isComputerNameExist(computerDto.getName())) {
                bindingResult.rejectValue("name", "error.name", "Tên máy đã tồn tại");
            }

            // Nếu có lỗi, trả về form `createRoomForm` và hiển thị lỗi
            if (bindingResult.hasErrors()) {
                return "createComputerForm";
            }

            ComputerDto createdComputer = computerService.createComputer(computerDto);

            redirectAttributes.addFlashAttribute("message", "Thêm máy tính thành công!");
            return "redirect:/computer/" + createdComputer.getId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi thêm máy tính");
            return "redirect:/computer/createComputerForm?error=true";
        }
    }
}
