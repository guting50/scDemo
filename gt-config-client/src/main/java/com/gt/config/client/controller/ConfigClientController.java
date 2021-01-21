package com.gt.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigClientController {

    @Value("${foo}")
    String foo;

    @GetMapping(value = "/getConfig")
    public String hi() {
        return foo;
    }
}
