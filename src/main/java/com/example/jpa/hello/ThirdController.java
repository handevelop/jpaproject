package com.example.jpa.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ThirdController {

    @GetMapping("/helloworld")
    public String helloRestApi() {
        return "hello rest api";
    }

}
