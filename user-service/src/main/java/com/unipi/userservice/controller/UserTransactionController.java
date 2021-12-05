package com.unipi.userservice.controller;

import com.unipi.userservice.dto.TransactionRequestDto;
import com.unipi.userservice.dto.TransactionResponseDto;
import com.unipi.userservice.model.UserTransaction;
import com.unipi.userservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/transaction")
public class UserTransactionController {

    private final TransactionService transactionService;

    @Autowired
    public UserTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public Flux<UserTransaction> getAllUserTransactionsById (@RequestParam("userId") int userId) {
        return this.transactionService.getTransactionsByUserId(userId);
    }

    @PostMapping
    public Mono<TransactionResponseDto> createTransaction(@RequestBody Mono<TransactionRequestDto> requestDtoMono) {
        return requestDtoMono.flatMap(this.transactionService::createTransaction);
    }
}
