package com.unipi.webfluxdemo.service;

import com.unipi.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MathService {

    // Find the square of input number
    public Response findSquare(int input) {
        return new Response(input * input);
    }

    // Multiplication table
    public List<Response> multiplicationTable(int input) {
        return IntStream.rangeClosed(1, 10).mapToObj(i -> new Response(i * input))
                .peek(i -> SleepUtil.sleepSeconds(1))
                .peek(i -> System.out.println("Math-Service processing: " + i))
                .collect(Collectors.toList());
    }
}
