package com.gt.server1.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：打招呼的一个对象
 */

@RestController
public class Controller1 {
    @Value("${server.port}")
    String port;

    @GetMapping("/sayHello1")
    public String sayHello1(@RequestParam(value = "name", defaultValue = "gt") String name) {
        return "hello service-1 " + name + " ,i am from port:" + port;
    }

}
