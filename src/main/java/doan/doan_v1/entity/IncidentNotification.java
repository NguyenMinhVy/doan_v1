package doan.doan_v1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "incident_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentNotification extends AbstractEntity{

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String message;

    @Column
    private int incidentId;

    @Column
    private int receiveId;

    @Column
    private Boolean delFlag;


}
