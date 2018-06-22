package com.example.casclient3.connect;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyLogin {
    @RequestMapping("login1")
    public String login1(){
        return "hello world";
    }

}
