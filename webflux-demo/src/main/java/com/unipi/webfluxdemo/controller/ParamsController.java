package com.unipi.webfluxdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/jobs")
public class ParamsController {

    // Dummy API to UnitTest ParamsTest
    @GetMapping("/search")
    public Flux<Integer> searchJobs(@RequestParam("count") int count,
                                    @RequestParam("page") int page) {
        // 'Just' is used only for this example, not recommended
        return Flux.just(count, page);
    }
}
