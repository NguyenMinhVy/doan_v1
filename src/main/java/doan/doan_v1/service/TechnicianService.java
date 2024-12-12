package doan.doan_v1.service;

import doan.doan_v1.dto.TechnicianDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TechnicianService {
//    List<TechnicianDto> getTechnicianDtoListByLocationId(int locationId);

//    TechnicianDto getTechnicianDtoById(Integer id);

    TechnicianDto getTechnicianDtoById(Integer id);

    List<TechnicianDto> getAllTechnicianDto();

    TechnicianDto getTechnicianDtoById(int id);

    String createTechnician(TechnicianDto technicianDto);

//    TechnicianDto getTechnicianDtoByUserId(int id);


    TechnicianDto getTechnicianDtoByUserId(Integer userId);

    List<TechnicianDto> getTechnicianDtoListByLocationId(Integer locationId);

    @Transactional
    void updateTechnicianLocations(Integer technicianId, List<Integer> locationIds);

    boolean isTechnicianInLocation(Integer technicianId, Integer locationId);

    void updateTechnician(TechnicianDto technicianDto);

    @Transactional
    void deleteTechnician(Integer id);
}
