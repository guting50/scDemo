package com.gt.feign.serverfeign;

import com.gt.feign.hystrix.Service1Hystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-1", fallback = Service1Hystrix.class)
public interface FeignService1 {

    @GetMapping("/sayHello1")
    String sayHello1(@RequestParam(value = "name") String name);
}
