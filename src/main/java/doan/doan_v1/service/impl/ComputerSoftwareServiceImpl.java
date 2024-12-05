package doan.doan_v1.service.impl;

import doan.doan_v1.dto.ComputerSoftwareDto;
import doan.doan_v1.entity.ComputerSoftware;
import doan.doan_v1.mapper.ComputerSoftwareMapper;
import doan.doan_v1.repository.ComputerSoftWareRepository;
import doan.doan_v1.service.ComputerSoftwareService;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComputerSoftwareServiceImpl implements ComputerSoftwareService {

    @Autowired
    private ComputerSoftWareRepository computerSoftwareRepository;

    @Autowired
    private ComputerSoftwareMapper computerSoftwareMapper;

    @Autowired
    private SoftWareService softWareService;

    @Override
    public List<ComputerSoftwareDto> getSoftwareByComputerId(int computerId) {
        List<ComputerSoftware> computerSoftwares = computerSoftwareRepository.findByComputerId(computerId);
        List<ComputerSoftwareDto> computerSoftwareDtos = new ArrayList<>();
        for (ComputerSoftware computerSoftware : computerSoftwares) {
            ComputerSoftwareDto computerSoftwareDto = new ComputerSoftwareDto();
            computerSoftwareDto = computerSoftwareMapper.ComputerSoftwareToComputerSoftwareDto(computerSoftware);
            computerSoftwareDto.setName(softWareService.getSoftWareDtoById(computerSoftware.getSoftwareId()).getName());
            computerSoftwareDtos.add(computerSoftwareDto);
        }
        return computerSoftwareDtos;
    }

    @Override
    public ComputerSoftwareDto getSoftwareById(int id) {
        ComputerSoftware computerSoftware = computerSoftwareRepository.findById(id).orElse(null);
        ComputerSoftwareDto computerSoftwareDto = computerSoftwareMapper.ComputerSoftwareToComputerSoftwareDto(computerSoftware);
        assert computerSoftware != null;
        computerSoftwareDto.setName(softWareService.getSoftWareDtoById(computerSoftware.getSoftwareId()).getName());

        return computerSoftwareDto;
    }
} 