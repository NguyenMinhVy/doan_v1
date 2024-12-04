package doan.doan_v1.service;

import doan.doan_v1.dto.LecturerDto;

import java.util.List;

public interface LecturerService {
    String createLecturer(LecturerDto lecturerDto);

    List<LecturerDto> getAllLecturerDto();

    LecturerDto getLecturerDtoById(int id);

    LecturerDto getLecturerDtoByUserId(int userId);

    LecturerDto updateLecturerDto(LecturerDto lecturerDto);
}