package com.example.springBoot.controllers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library")
public class HomeController {
    @GetMapping()
    public String homePage(){
        return "index";
    } //просто вывод двух кнопок с переходом на основные странички
}
