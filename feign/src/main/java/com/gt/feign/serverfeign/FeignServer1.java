package com.gt.feign.serverfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "service-1")
public interface FeignServer1 {

    @GetMapping("/hello1")
    public String sayHello1();
}
