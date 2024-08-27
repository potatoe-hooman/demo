package com.assignment.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.demo.model.DemoResponseType;
import com.assignment.demo.service.DemoService;

@RestController
@RequestMapping("/api/home")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/data")
    public ResponseEntity<DemoResponseType> getData() {
        DemoResponseType response = demoService.getDataFromExternalApi();
        return ResponseEntity.ok(response);
    }
}
