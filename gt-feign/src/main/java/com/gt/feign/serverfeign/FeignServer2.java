package com.gt.feign.serverfeign;

import com.gt.feign.hystrix.Service2Hystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "server-2", fallback = Service2Hystrix.class)
public interface FeignServer2 {

    @GetMapping("/sayHello2")
    String sayHello2(@RequestParam(value = "name") String name);
}
