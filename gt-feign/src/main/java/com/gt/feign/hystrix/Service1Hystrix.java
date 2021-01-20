package com.gt.feign.hystrix;

import com.gt.feign.serverfeign.FeignServer1;
import org.springframework.stereotype.Component;

@Component
public class Service1Hystrix implements FeignServer1 {

    @Override
    public String sayHello1(String name) {
        return "sorry sayHello1 " + name;
    }
}
