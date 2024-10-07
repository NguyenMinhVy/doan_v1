package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "software")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Software extends AbstractEntity{

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String version;

    @Column
    private LocalDateTime setupTime;

    @Column
    private Boolean delFlag = false;
}
