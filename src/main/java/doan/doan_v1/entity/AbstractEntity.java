package doan.doan_v1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @JsonIgnore
    @Column(name="insert_id", length = 16, nullable = true, unique = false)
    private Integer insertId;

    @JsonIgnore
    @Column(name="insert_date", nullable = true, unique = false)
    private LocalDateTime insertDate;

    @JsonIgnore
    @Column(name="update_id", length = 16, nullable = true, unique = false)
    private Integer updateId;

    @JsonIgnore
    @Column(name="update_date", nullable = true, unique = false)
    private LocalDateTime updateDate;

}
