package doan.doan_v1.service.impl;

import doan.doan_v1.Constant.Constant;
import doan.doan_v1.dto.*;
import doan.doan_v1.entity.*;
import doan.doan_v1.mapper.ComputerMapper;
import doan.doan_v1.repository.ComputerDeviceRepository;
import doan.doan_v1.repository.ComputerRepository;
import doan.doan_v1.repository.ComputerSoftWareRepository;
import doan.doan_v1.repository.RoomRepository;
import doan.doan_v1.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComputerServiceImpl implements ComputerService {

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private ComputerMapper computerMapper;

    @Autowired
    private ComputerDeviceRepository computerDeviceRepository;

    @Autowired
    private ComputerSoftWareRepository computerSoftWareRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SoftWareService softWareService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Override
    public List<ComputerDto> getComputerListByRoomId(int roomId) {
        List<Computer> computerList = computerRepository.findByRoomIdAndDelFlagFalse(roomId);
        if (computerList.isEmpty()) {
            return Collections.emptyList();
        }
        return computerMapper.computerListToComputerDtoList(computerList);
    }

    @Override
    public ComputerDto getComputerById(int id) {
//        ComputerDto computerDto = new ComputerDto();
        Computer computer = computerRepository.findById(id).orElse(null);
        if (computer == null) {
            return null;
        }
//        List<DeviceDto> deviceDtos = deviceService.findAllDeviceDtoByComputerId(id);
//        List<SoftWareDto> softWareDtos = softWareService.getSoftWareDtoListByComputerId(id);
//
//        computerDto.setDeviceIdList(deviceDtos.stream().map(DeviceDto::getId).collect(Collectors.toList()));
//        computerDto.setSoftWareIdList(softWareDtos.stream().map(SoftWareDto::getId).collect(Collectors.toList()));

        return computerMapper.computerToComputerDto(computer);
    }

    @Override
    public boolean isComputerNameExist(String name) {
        Computer computer = computerRepository.findComputerByNameAndDelFlagFalse(name);
        return computer != null;
    }

    @Override
    public ComputerDto createComputer(ComputerDto computerDto) {
        Computer computer = computerMapper.computerDtoToComputer(computerDto);
        computerRepository.save(computer);
        computer.setId(computer.getId());
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        List<SoftWareDto> softwareDtoList = new ArrayList<>();
        for (Integer deviceId : computerDto.getDeviceIdList()) {
            DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
            deviceDtoList.add(deviceDto);
        }
        for (Integer softWareId : computerDto.getSoftWareIdList()) {
            SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softWareId);
            softWareDto.setStatus(Constant.STATUS.OK);
            softwareDtoList.add(softWareDto);
        }

        saveComputerDeviceList(deviceDtoList, computer);
        saveComputerSoftwareList(softwareDtoList, computer);
        return computerMapper.computerToComputerDto(computer);
    }

    @Override
    public List<ComputerDto> createManyComputer(ComputerDto computerDto) {
        RoomDto roomDto = roomService.getRoomById(computerDto.getRoomId());
        int quantity = roomDto.getRoomTypeDto().getCapacity();
        List<Computer> computerList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Computer computer = new Computer();
            String formattedIndex = String.format("%02d", i + 1);

            computer.setName(roomDto.getName().replace(".", "")+ "M" + formattedIndex);
            computer.setStatus(Constant.STATUS.OK);
            computer.setRoomId(roomDto.getId());
            computerList.add(computer);
        }
        computerRepository.saveAll(computerList);

        List<DeviceDto> deviceDtoList = new ArrayList<>();
        List<SoftWareDto> softwareDtoList = new ArrayList<>();
        for (Integer deviceId : computerDto.getDeviceIdList()) {
            DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
            deviceDtoList.add(deviceDto);
        }
        for (Integer softWareId : computerDto.getSoftWareIdList()) {
            SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softWareId);
            softWareDto.setStatus(Constant.STATUS.OK);
            softwareDtoList.add(softWareDto);
        }

        for (Computer computer : computerList) {
            saveComputerDeviceList(deviceDtoList, computer);
            saveComputerSoftwareList(softwareDtoList, computer);
        }

        return computerMapper.computerListToComputerDtoList(computerList);
    }

    @Override
    public void updateComputer(int id, ComputerDto computerDto) {
        List<DeviceDto> deviceDtoList = new ArrayList<>();
        List<SoftWareDto> softwareDtoList = new ArrayList<>();

        Computer computer = computerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy máy tính với ID: " + id));

        computer.setName(computerDto.getName());
        computer.setStatus(computerDto.getStatus());

        Room room = roomRepository.findById(computerDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + computerDto.getRoomId()));
        computer.setRoomId(room.getId());

        List<Integer> oldComputerDeviceID = computerDeviceRepository.findByComputerId(computer.getId()).stream().map(ComputerDevice::getDeviceId).toList();
        List<Integer> oldComputerSoftwareID = computerSoftWareRepository.findByComputerId(computer.getId()).stream().map(ComputerSoftware::getSoftwareId).toList();

        List<ComputerDevice> oldComputerDevicesInactive = computerDeviceRepository.findByComputerId(computer.getId()).stream()
                .filter(item -> !computerDto.getDeviceIdList().contains(item.getDeviceId())).collect(Collectors.toList());

        List<ComputerSoftware> oldComputerSoftwaresInactive = computerSoftWareRepository.findByComputerId(computer.getId()).stream()
                .filter(item -> !computerDto.getSoftWareIdList().contains(item.getSoftwareId())).collect(Collectors.toList());

        computerDeviceRepository.deleteAll(oldComputerDevicesInactive);
        computerSoftWareRepository.deleteAll(oldComputerSoftwaresInactive);

        List<Integer> newComputerDeviceId = computerDto.getDeviceIdList().stream().filter(item -> !oldComputerDeviceID.contains(item)).toList();
        List<Integer> newSoftwareDeviceId = computerDto.getSoftWareIdList().stream().filter(item -> !oldComputerSoftwareID.contains(item)).toList();

        for (Integer deviceId : newComputerDeviceId) {
            DeviceDto deviceDto = deviceService.findDeviceDtoById(deviceId);
            deviceDtoList.add(deviceDto);
        }
        for (Integer softWareId : newSoftwareDeviceId) {
            SoftWareDto softWareDto = softWareService.getSoftWareDtoById(softWareId);
            softwareDtoList.add(softWareDto);
        }

        saveComputerDeviceList(deviceDtoList, computer);
        saveComputerSoftwareList(softwareDtoList, computer);

        computerRepository.save(computer);
    }

    private void saveComputerSoftwareList(List<SoftWareDto> softwareDtoList, Computer computer) {
        for (SoftWareDto software : softwareDtoList) {
            ComputerSoftware computerSoftware = new ComputerSoftware();
            computerSoftware.setSoftwareId(software.getId());
            computerSoftware.setComputerId(computer.getId());
            computerSoftware.setStatus(Constant.STATUS.OK);
            computerSoftWareRepository.save(computerSoftware);
        }
    }

    private void saveComputerDeviceList(List<DeviceDto> deviceDtoList, Computer computer) {
        for (DeviceDto device : deviceDtoList) {
            ComputerDevice computerDevice = new ComputerDevice();
            computerDevice.setDeviceId(device.getId());
            computerDevice.setComputerId(computer.getId());
            computerDevice.setStatus(Constant.STATUS.OK);
            int type = device.getType();
            setDeviceCode(device, type, computerDevice, computer);
            computerDeviceRepository.save(computerDevice);
        }
    }

    @Override
    public void deleteComputer(int id) {
        Computer computer = computerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy máy tính với ID: " + id));
        computer.setDelFlag(true);
        computerRepository.save(computer);
    }


    private static void setDeviceCode(DeviceDto device, int type, ComputerDevice computerDevice, Computer computer) {
        switch (type) {
            case Constant.DEVICE_TYPE.KEY:
                computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.KEY + "-" + computer.getId() + "-" + device.getId());
                break;
            case Constant.DEVICE_TYPE.MOU:
                computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.MOU + "-" + computer.getId() + "-" + device.getId());
                break;
            case Constant.DEVICE_TYPE.SCREEN:
                computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.SCREEN + "-" + computer.getId() + "-" + device.getId());
                break;
            case Constant.DEVICE_TYPE.CASE:
                computerDevice.setDeviceCode(Constant.DEVICE_TYPE_STR.CASE + "-" + computer.getId() + "-" + device.getId());
                break;
        }
    }

}

