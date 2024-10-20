package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "incident")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Incident extends AbstractEntity{

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String description;

    @Column
    private int status;

    @Column
    private LocalDateTime reportDate;

    @Column
    private int reportUser;

    @Column
    private int softwareId;

    @Column
    private int deviceId;

    @Column
    private Boolean delFlag = false;
}