package doan.doan_v1.repository;

import doan.doan_v1.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    RoomType findByIdAndDelFlagFalse(int roomTypeId);
}
