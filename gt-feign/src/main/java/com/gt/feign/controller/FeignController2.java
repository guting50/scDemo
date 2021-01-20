package com.gt.feign.controller;

import com.gt.feign.serverfeign.FeignServer2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ Description：打招呼的一个对象
 */

@ResponseBody
@RestController
public class FeignController2 {
    //编译器报错，无视。 因为这个Bean是在程序启动的时候注入的，编译器感知不到，所以报错。
    @Autowired
    FeignServer2 feignServer2;

    @RequestMapping(method = RequestMethod.GET, value = "/sayHello2")
    public String sayHello2(@RequestParam(value = "name") String name) {
        return feignServer2.sayHello2(name);
    }
}
