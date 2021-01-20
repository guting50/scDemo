package com.gt.feign.serverfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(value = "server-2")
public interface FeignServer2 {

    @GetMapping("/sayHello2")
    String sayHello2(@RequestParam(value = "name") String name);
}
