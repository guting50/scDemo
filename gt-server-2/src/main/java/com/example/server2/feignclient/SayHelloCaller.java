package com.example.server2.feignclient;
/**
 * @ Description：调用service-2的SayHello类的方法
 */

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 调用者的接口方法是被调用者方法的声明，映射路径与被调用者方法的映射路径相同
 */
@FeignClient(value = "server-2") //调用的服务名称
public interface SayHelloCaller {
    @GetMapping("/hello")
    public String sayHello();
}