package doan.doan_v1.controller;

import doan.doan_v1.dto.DeviceDto;
import doan.doan_v1.dto.IncidentDto;
import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.service.DeviceService;
import doan.doan_v1.service.IncidentService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/incident")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;


    @GetMapping("/device/{deviceId}")
    public String getIncidentDetailByDeviceId (@PathVariable("deviceId") int deviceId, Model model) {
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoListByDeviceId(deviceId);
        DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
        model.addAttribute("deviceDto", deviceDto);
        model.addAttribute("incidentDtoList", incidentDtoList);
        return "incident";
    }

    @GetMapping("/software/{softwareId}")
    public String getIncidentDetailBySoftwareId (@PathVariable("softwareId") int softwareId, Model model) {
        List<IncidentDto> incidentDtoList = incidentService.getIncidentDtoListBySoftwareId(softwareId);
        SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softwareId);
        model.addAttribute("softWareDto", softWareDto);
        model.addAttribute("incidentDtoList", incidentDtoList);
        return "incident";
    }

    @GetMapping("/add")
    public String addIncident (IncidentDto incidentDtoRequest , Model model) {
        IncidentDto incidentDto = incidentService.addIncident(incidentDtoRequest);
        model.addAttribute("incidentDto", incidentDto);
        return "incident";
    }
}
