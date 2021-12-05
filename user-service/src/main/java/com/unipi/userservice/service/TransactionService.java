package com.unipi.userservice.service;

import com.unipi.userservice.dto.TransactionRequestDto;
import com.unipi.userservice.dto.TransactionResponseDto;
import com.unipi.userservice.dto.TransactionStatus;
import com.unipi.userservice.model.UserTransaction;
import com.unipi.userservice.repository.UserRepository;
import com.unipi.userservice.repository.UserTransactionRepository;
import com.unipi.userservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final UserTransactionRepository transactionRepository;

    @Autowired
    public TransactionService(UserRepository userRepository, UserTransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto requestDto) {
        return this.userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
                .filter(Boolean::booleanValue) // this pipeline will continue only if true
                .map(b -> EntityDtoUtil.toEntity(requestDto))
                .flatMap(this.transactionRepository::save)
                .map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
                .defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
    }

    public Flux<UserTransaction> getTransactionsByUserId(final int userId) {
        return this.transactionRepository.getTransactionsByUserId(userId);
    }
}
