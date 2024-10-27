package doan.doan_v1.service.impl;

import doan.doan_v1.dto.SoftWareDto;
import doan.doan_v1.entity.ComputerSoftware;
import doan.doan_v1.entity.Software;
import doan.doan_v1.mapper.SoftWareMapper;
import doan.doan_v1.repository.ComputerSoftWareRepository;
import doan.doan_v1.repository.SoftWareRepository;
import doan.doan_v1.service.SoftWareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoftWareServiceImpl implements SoftWareService {

    @Autowired
    SoftWareRepository softWareRepository;

    @Autowired
    private ComputerSoftWareRepository computerSoftWareRepository;

    @Autowired
    private SoftWareMapper softWareMapper;

    @Override
    public List<SoftWareDto> getSoftWareDtoListByComputerId(int computerId) {
        List<SoftWareDto> softWareDtoList = new ArrayList<>();
        List<ComputerSoftware> computerSoftwareList = computerSoftWareRepository.findByComputerId(computerId);
        for (ComputerSoftware computerSoftware : computerSoftwareList) {
            Software software = softWareRepository.findByIdAndDelFlagFalse(computerSoftware.getSoftwareId());
            if (software != null) {
                SoftWareDto softWareDto = softWareMapper.softWareToSoftWareDto(software);
                softWareDto.setStatus(computerSoftware.getStatus());
                softWareDto.setComputerId(computerSoftware.getComputerId());
                softWareDtoList.add(softWareDto);
            }
        }
        return softWareDtoList;
    }

    @Override
    public SoftWareDto getSoftWareDtoById(int softWareId) {
        Software software = softWareRepository.findById(softWareId).orElse(null);
        return softWareMapper.softWareToSoftWareDto(software);
    }

    @Override
    public List<SoftWareDto> getAllSoftWareDtoList() {

        return softWareRepository.findByDelFlagFalse()
                .stream()
                .map(softWareMapper::softWareToSoftWareDto)
                .collect(Collectors.toList());
    }
}
