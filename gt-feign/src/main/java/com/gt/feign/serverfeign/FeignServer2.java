package com.gt.feign.serverfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@FeignClient(value = "service-2")
public interface FeignServer2 {

    @RequestMapping(method = RequestMethod.GET, value = "/sayHello2")
    public String sayHello2(@RequestParam(value = "name") String name);
}
