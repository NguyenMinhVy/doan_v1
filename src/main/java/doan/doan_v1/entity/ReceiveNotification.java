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
@Table(name = "receive_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiveNotification extends AbstractEntity{

    @Id
    @Column
    private int userId;

    @Id
    @Column
    private int incidentNotificationId;

}
