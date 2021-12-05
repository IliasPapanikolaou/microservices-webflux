package com.unipi.userservice.util;

import com.unipi.userservice.dto.TransactionRequestDto;
import com.unipi.userservice.dto.TransactionResponseDto;
import com.unipi.userservice.dto.TransactionStatus;
import com.unipi.userservice.dto.UserDto;
import com.unipi.userservice.model.User;
import com.unipi.userservice.model.UserTransaction;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

public class EntityDtoUtil {

    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public static User toEntity(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }

    public static UserTransaction toEntity(TransactionRequestDto requestDto) {
        UserTransaction userTransaction = new UserTransaction();
        userTransaction.setUserId(requestDto.getUserId());
        userTransaction.setAmount(requestDto.getAmount());
        userTransaction.setTransactionDate(LocalDateTime.now());
        return userTransaction;
    }

    public static TransactionResponseDto toDto(TransactionRequestDto requestDto, TransactionStatus status) {
        TransactionResponseDto responseDto = new TransactionResponseDto();
        responseDto.setAmount(requestDto.getAmount());
        responseDto.setUserId(requestDto.getUserId());
        responseDto.setStatus(status);
        return responseDto;
    }

}
