package com.example.aneukbeserver;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Tag(name="TestController")
@RequestMapping("/")
@RestController
public class TestController {



    @Operation(summary = "test api", description = "testtesttest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = {@Content(schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "404", description = "errorororor"),
    })
    @GetMapping
    @ResponseBody
    public ResponseEntity<String> test() {
        String message = "SUCCESS?!?!?!?";
        return ResponseEntity.ok(message);
    }


    @GetMapping("/test")
    public List<String> test2() {
        RestTemplate restTemplate = new RestTemplate();
        String externalContent = restTemplate.getForObject("http://server-fastapi:8000/test", String.class);
        System.out.println(externalContent);
        return List.of("sprint api content", externalContent);
    }
}
