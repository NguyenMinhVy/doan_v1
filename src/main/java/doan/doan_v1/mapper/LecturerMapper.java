package doan.doan_v1.mapper;

import doan.doan_v1.dto.LecturerDto;
import doan.doan_v1.entity.Lecturer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LecturerMapper {
    Lecturer lectureDtoToLecturer(LecturerDto lectureDto);
    LecturerDto lectureToLecturerDto(Lecturer lecture);

    List<Lecturer> lectureDtoListToLecturerList(List<LecturerDto> lectureDtoList);
    List<LecturerDto> lectureListToLecturerDtoList(List<Lecturer> lectureList);

}
