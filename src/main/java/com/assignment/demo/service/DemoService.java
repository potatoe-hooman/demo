package com.assignment.demo.service;

import com.assignment.demo.model.DemoResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DemoService {

    @Autowired
    private RestTemplate restTemplate;

    public DemoResponseType getDataFromExternalApi() {
        String url = "https://jsonplaceholder.typicode.com/posts/1";
        return restTemplate.getForObject(url, DemoResponseType.class);
    }
}
