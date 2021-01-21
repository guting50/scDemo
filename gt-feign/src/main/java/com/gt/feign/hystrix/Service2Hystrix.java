package com.gt.feign.hystrix;

import com.gt.feign.serverfeign.FeignService2;
import org.springframework.stereotype.Component;

@Component
public class Service2Hystrix implements FeignService2 {

    @Override
    public String sayHello2(String name) {
        return "sorry sayHello2 " + name;
    }
}
