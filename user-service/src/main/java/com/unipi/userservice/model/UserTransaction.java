package com.unipi.userservice.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

// Note: @Entity doesn't work for R2DBC
@Data
@ToString
// Name table differently than default
@Table("transactions")
public class UserTransaction {

    @Id
    private Integer id;
    private Integer userId;
    private Double amount;
    private LocalDateTime transactionDate;
}
