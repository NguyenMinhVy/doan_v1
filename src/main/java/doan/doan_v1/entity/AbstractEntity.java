package doan.doan_v1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

    @JsonIgnore
    @Column(name="insert_id", length = 16, nullable = false, unique = false)
    private int insertId;

    @JsonIgnore
    @Column(name="insert_date", nullable = false, unique = false)
    private Timestamp insertDate;

    @JsonIgnore
    @Column(name="update_id", length = 16, nullable = false, unique = false)
    private int updateId;

    @JsonIgnore
    @Column(name="update_date", nullable = false, unique = false)
    private Timestamp updateDate;

}
