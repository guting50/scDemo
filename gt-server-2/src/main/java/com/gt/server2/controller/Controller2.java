package com.gt.server2.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：打招呼的一个对象
 */

@RestController
public class Controller2 {
    @Value("${server.port}")
    String port;

    @GetMapping("/sayHello2")
    public String sayHello2(@RequestParam(value = "name", defaultValue = "gt") String name) {
        return "hello server-2 " + name + " ,i am from port:" + port;
    }
}
