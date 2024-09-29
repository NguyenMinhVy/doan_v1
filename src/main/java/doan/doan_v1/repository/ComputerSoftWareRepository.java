package doan.doan_v1.repository;

import doan.doan_v1.entity.ComputerSoftware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerSoftWareRepository extends JpaRepository<ComputerSoftware, Integer> {

    List<ComputerSoftware> findByComputerId(int computerId);

}
