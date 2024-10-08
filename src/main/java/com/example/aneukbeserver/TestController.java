package com.example.aneukbeserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/")
@RestController
public class TestController {
    @GetMapping
    public String test() {
        return "SUCCECC?!?!?!?";
    }

    @GetMapping("/test")
    public List<String> test2() {
        return List.of("SUCCECC?!?!?!?", "SUCCECC?!?!?!?", "SUCCECC?!?!?!?");
    }
}
