package com.gt.server1.controller;

import com.gt.server2.feignclient.SayHelloCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：打招呼的一个对象
 */

@RestController
public class Person {
    @Autowired
    private SayHelloCaller caller;

    @GetMapping("/sayHello")
    public String sayHello() {
        return "server-1 " + caller.sayHello();
    }

    @GetMapping("/sayHello1")
    public String sayHello1() {
        return "server-1  hello";
    }
}
