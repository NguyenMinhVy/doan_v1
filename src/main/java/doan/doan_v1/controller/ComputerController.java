package doan.doan_v1.controller;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.*;
import doan.doan_v1.service.ComputerService;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
    private IncidentService incidentService;


    @GetMapping("/{computerId}")
    public String getComputerDetail (@PathVariable("computerId") int computerId, Model model) {
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoByComputerId(computerId);
        List<SoftWareDto> softWareDtoList = softWareService.getSoftWareDtoListByComputerId(computerId);
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoListByComputerId(computerId);
        List<IncidentDto> incidentDtoListByStatus = new ArrayList<>();
        incidentDtoListByStatus.addAll(incidentService.getIncidentDtoListByStatus(incidentDtoList, Constant.INCIDENT_STATUS.UNPROCESSED));
        incidentDtoListByStatus.addAll(incidentService.getIncidentDtoListByStatus(incidentDtoList, Constant.INCIDENT_STATUS.PROCESSING));
        incidentDtoListByStatus.sort((o1, o2) -> Long.compare(o2.getId(), o1.getId()));
        model.addAttribute("softWareDtoList", softWareDtoList);
        model.addAttribute("deviceDtoList", deviceDtoList);
        model.addAttribute("incidentDtoListByStatus", incidentDtoListByStatus);
        return "computer";
    }

    @GetMapping("/createComputerForm")
    public String createComputerForm (@RequestParam("roomId") int roomId, Model model) {
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
    public String createRoom(@ModelAttribute ComputerDto computerDto, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
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
}
