package com.example.casclient3.cookie;

import org.apereo.cas.CentralAuthenticationService;
import org.apereo.cas.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyCookieController {

    @Autowired
    @Qualifier("centralAuthenticationService")
    private CentralAuthenticationService centralAuthenticationService;
    @Autowired
    MyCookieGenerator myCookieGenerator;

    @GetMapping("/setcookie")
    public String setCookie(@RequestParam("service") String service, @RequestParam("tgt") String tgt) {
        try {
            // 校验
            Ticket ticket = this.centralAuthenticationService.getTicket(tgt, Ticket.class);
            if (ticket == null || ticket.isExpired()) {
                // 无效tgt，跳转到登录
                return "redirect:/login";
            }
            // 添加cookie
            myCookieGenerator.addCookie(tgt);
            // 跳转到客户端
            return "redirect:" + service;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/login";
    }
}
