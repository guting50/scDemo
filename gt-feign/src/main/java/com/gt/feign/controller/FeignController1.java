package com.gt.feign.controller;

import com.gt.feign.serverfeign.FeignService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：打招呼的一个对象
 */

@RestController
public class FeignController1 {
    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    FeignService1 feignService1;

    @GetMapping("/sayHello1")
    public String sayHello1(String name) {
        return feignService1.sayHello1(name);
    }
}
