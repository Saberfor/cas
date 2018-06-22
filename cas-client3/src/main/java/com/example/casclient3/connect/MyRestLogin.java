package com.example.casclient3.connect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyRestLogin {

    @GetMapping("restLogin1")
    public String login1(){
        return "hello world";
    }
}
