package com.example.casclient3.connect;

import com.example.casclient3.MyModel.Capcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MyRegister {

    @RequestMapping("myRegister1")
    public String myRegister1(){
        return "myRegister1";
    }

    @GetMapping(value = "/capcha")
    public String capcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置响应的类型格式为图片格式
        response.setContentType("image/jpeg");
        //禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession();

        Capcha capcha = new Capcha(120, 40, 5, 100);
        session.setAttribute("capcha", capcha.getCode());
        capcha.write(response.getOutputStream());
        return null;
    }
}
