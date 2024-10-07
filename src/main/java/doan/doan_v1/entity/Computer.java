package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "computer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Computer extends AbstractEntity{
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private int roomId;

    @Column
    private int status;

    @Column
    private Boolean delFlag = false;
}
