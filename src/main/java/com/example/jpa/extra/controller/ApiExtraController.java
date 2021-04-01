package com.example.jpa.extra.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiExtraController {

    @GetMapping("/extra/pharmacy")
    public String pharmacy() {

        String serviceKey = "444T70uHPYdXnZ%2B0q%2Fv0wKcypwdztoN88jp5A0rYJ%2FafC13C1BSjRyYi%2BWPTSC03FOJh%2F6EYtin1EG9cWBXqzA%3D%3D";
        String url = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown?serviceKey=%s&pageNo=1&numOfRows=10";

        String result = "";
        try {
            URI uri = new URI(String.format(url, serviceKey));

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            result = restTemplate.getForObject(uri, String.class);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
