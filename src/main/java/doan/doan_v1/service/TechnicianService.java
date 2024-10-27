package doan.doan_v1.service;

import doan.doan_v1.dto.TechnicianDto;

import java.util.List;

public interface TechnicianService {
    List<TechnicianDto> getTechnicianDtoListByLocationId(int locationId);

    List<TechnicianDto> getAllTechnicianDto();

    TechnicianDto getTechnicianDtoById(int id);
}
