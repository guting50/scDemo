package com.gt.feign.serverfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service-2")
public interface FeignServer2 {

    @GetMapping("/hello2")
    public String sayHello2();
}
