package com.example.jpa.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


// ResponseBody 를 사용하지 않고 Rest 형식으로 사용 가능하게 해줌
@RestController
public class SecondController {

    @RequestMapping(value = "/hello-spring", method = RequestMethod.GET)
    public String helloSpring() {
        return "hello spring";
    }

    // GetMapping 이 더 명확함
    @GetMapping("/hello-rest")
    public String helloRest() {
        return "hello rest";
    }
}
