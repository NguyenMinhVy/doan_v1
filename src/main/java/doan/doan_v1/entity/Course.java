package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends AbstractEntity{

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column
    private String courseCode;

    @Column
    private String className;

    @Column
    private int numOfLesson;

    @Column
    private String duration;

    @Column
    private int roomId;

    @Column
    private Boolean delFlag = false;
}