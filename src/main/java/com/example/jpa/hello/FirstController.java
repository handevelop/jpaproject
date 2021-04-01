package com.example.jpa.hello;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {

    @RequestMapping(value = "/first-url", method = RequestMethod.GET)
    public void first() {

    }

    // default 는 GET 방식이기에 생략 가능
    // ResponseBody 를 넣으면 디폴트인 뷰페이지 리턴이 아닌 스트링을 리턴할 수 있다.
    @ResponseBody
    @RequestMapping("/helloworld")
    public String helloWorld() {
        return "hello world";
    }

}
