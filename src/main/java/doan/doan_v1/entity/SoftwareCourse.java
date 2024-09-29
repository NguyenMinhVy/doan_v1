package doan.doan_v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "software_course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoftwareCourse extends AbstractEntity{

    @Id
    @Column
    private int softwareId;

    @Id
    @Column
    private int courseId;
}
