package com.yt.plan.forecast.controller;

import com.yt.plan.forecast.cache.LogEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String home() {    
        System.out.println("Hello World!,这是为了测试提交");    
        return "Hello World!";
    }
}
