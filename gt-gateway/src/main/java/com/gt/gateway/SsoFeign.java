package com.gt.gateway;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "sso-server")
public interface SsoFeign {
    /**
     * 判断key是否存在
     */
    @RequestMapping("redis/hasKey/{key}")
    public Boolean hasKey(@PathVariable("key") String key);

    /**
     * 判断key是否存在,如果不存在，则跳转到登录页面
     */
    @RequestMapping("redis/hasKeyAndRedirect")
    public String hasKeyAndRedirect(@RequestParam(value = "key") String key);

}
