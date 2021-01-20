package com.gt.server2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：服务提供者提供打招呼的服务
 */
@RestController
public class SayHello {

    @GetMapping("/hello")
    public String sayHello() {
        return "server-2 hello";
    }
}
