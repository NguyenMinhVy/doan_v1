package doan.doan_v1.repository;

import doan.doan_v1.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {

    List<Lecturer> findByDelFlagFalse();

    Lecturer findByIdAndDelFlagFalse(Integer id);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Lecturer l " +
            "WHERE l.lectureCode = :lecturerCode AND l.delFlag = false")
    boolean existsByLecturerCodeAndDelFlagFalse(@Param("lecturerCode") String lecturerCode);

//    @Query("SELECT l FROM Lecturer l " +
//            "LEFT JOIN FETCH l.user " +
//            "WHERE l.delFlag = false")
//    List<Lecturer> findAllWithDetailsAndDelFlagFalse();
//
//    @Query("SELECT l FROM Lecturer l " +
//            "LEFT JOIN FETCH l.user " +
//            "WHERE l.id = :id AND l.delFlag = false")
//    Lecturer findByIdWithUserAndDelFlagFalse(@Param("id") Integer id);
}