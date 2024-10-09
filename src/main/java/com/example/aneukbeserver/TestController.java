package com.example.aneukbeserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
        RestTemplate restTemplate = new RestTemplate();
        String externalContent = restTemplate.getForObject("http://localhost:2518/test", String.class);
        System.out.println(externalContent);
        return List.of("sprint api content", externalContent);
    }
}
