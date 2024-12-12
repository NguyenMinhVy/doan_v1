package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "technician_location")
public class TechnicianLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "technician_id")
    private Integer technicianId;

    @Column(name = "location_id")
    private Integer locationId;
} 