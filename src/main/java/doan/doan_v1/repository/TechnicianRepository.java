package doan.doan_v1.repository;

import doan.doan_v1.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

    List<Technician> findByLocationId(Integer locationId);

    List<Technician> findByDelFlagFalse();

    Technician findByIdAndDelFlagFalse(Integer id);

    Technician findByUserIdAndDelFlagFalse(Integer userId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Technician t " +
            "WHERE t.technicianCode = :technicianCode AND t.delFlag = false")
    boolean existsByTechnicianCodeAndDelFlagFalse(@Param("technicianCode") String technicianCode);

//    @Query("SELECT t FROM Technician t WHERE t.location.id = :locationId AND t.delFlag = false")
//    List<Technician> findByLocationIdAndDelFlagFalse(@Param("locationId") Integer locationId);

//    @Query("SELECT t FROM Technician t " +
//            "LEFT JOIN FETCH t.user " +
//            "LEFT JOIN FETCH t.location " +
//            "WHERE t.delFlag = false")
//    List<Technician> findAllWithDetailsAndDelFlagFalse();
}
