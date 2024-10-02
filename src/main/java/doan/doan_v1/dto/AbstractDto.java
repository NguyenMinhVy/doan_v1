package doan.doan_v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;

import java.sql.Timestamp;

public class AbstractDto {
    private int insertId;
    private Timestamp insertDate;
    private int updateId;
    private Timestamp updateDate;
}
