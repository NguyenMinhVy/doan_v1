package doan.doan_v1.repository;

import doan.doan_v1.entity.ComputerSoftware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComputerSoftWareRepository extends JpaRepository<ComputerSoftware, Integer> {

    List<ComputerSoftware> findByComputerId(int computerId);

    Optional<ComputerSoftware> findById(Integer id);

    @Query("SELECT cs FROM ComputerSoftware cs " +
            "WHERE cs.computerId = :computerId " +
            "AND cs.softwareId = :softwareId")
    Optional<ComputerSoftware> findByComputerIdAndSoftwareId(
            @Param("computerId") Integer computerId,
            @Param("softwareId") Integer softwareId
    );

    @Query("SELECT CASE WHEN COUNT(cs) > 0 THEN true ELSE false END FROM ComputerSoftware cs " +
            "WHERE cs.computerId = :computerId " +
            "AND cs.softwareId = :softwareId")
    boolean existsByComputerIdAndSoftwareId(
            @Param("computerId") Integer computerId,
            @Param("softwareId") Integer softwareId
    );
}
