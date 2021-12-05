package com.unipi.orderservice.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionResponseDto {

    private Integer userId;
    private Double amount;
    private TransactionStatus status;

}
