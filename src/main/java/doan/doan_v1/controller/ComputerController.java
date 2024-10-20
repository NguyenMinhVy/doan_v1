package doan.doan_v1.controller;

import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/computer")
public class ComputerController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;


    @GetMapping("/{computerId}")
    public String getComputerDetail (@PathVariable("computerId") int computerId, Model model) {
        List<DeviceDto> deviceDtoList = deviceService.findAllDeviceDtoByComputerId(computerId);
        List<SoftWareDto> softWareDtoList = softWareService.getSoftWareDtoListByComputerId(computerId);
        model.addAttribute("softWareDtoList", softWareDtoList);
        model.addAttribute("deviceDtoList", deviceDtoList);
        return "computer";
    }
}
