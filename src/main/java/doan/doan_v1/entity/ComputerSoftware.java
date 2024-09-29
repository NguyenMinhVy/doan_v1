package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "computer_software")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComputerSoftware extends AbstractEntity{

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int computerId;

    @Column
    private int softwareId;

    @Column
    private int status;
}
