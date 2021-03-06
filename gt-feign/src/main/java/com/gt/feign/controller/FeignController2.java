package com.gt.feign.controller;

import com.gt.feign.serverfeign.FeignService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ Description：打招呼的一个对象
 */

@ResponseBody
@RestController
public class FeignController2 {
    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    FeignService2 feignService2;

    @GetMapping("/sayHello2")
    public String sayHello2(String name) {
        return feignService2.sayHello2(name);
    }
}
