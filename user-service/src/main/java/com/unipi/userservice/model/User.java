package com.unipi.userservice.model;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

// Note: @Entity doesn't work for R2DBC
@Data
@ToString
// Name table differently than default:
@Table("users")
public class User {

    @Id
    private Integer id;
    private String name;
    private Double balance;
}
