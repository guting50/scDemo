package com.gt.feign.serverfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(value = "server-1")
public interface FeignServer1 {

    @GetMapping("/sayHello1")
    String sayHello1(@RequestParam(value = "name") String name);
}
