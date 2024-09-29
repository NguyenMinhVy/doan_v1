package doan.doan_v1.repository;

import doan.doan_v1.entity.Software;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoftWareRepository extends JpaRepository<Software, Integer> {

    Software findByIdAndDelFlagFalse(int id);

}
