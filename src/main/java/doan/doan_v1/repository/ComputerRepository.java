package doan.doan_v1.repository;

import doan.doan_v1.entity.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends JpaRepository<Computer,Integer> {

    List<Computer> findByRoomIdAndDelFlagFalse(Integer roomId);
}
