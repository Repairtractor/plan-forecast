package com.yt.plan.forecast.controller;

import com.yt.plan.forecast.cache.LogEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @GetMapping("")
    public String home() {
        return "Hello World!";
    }
}
