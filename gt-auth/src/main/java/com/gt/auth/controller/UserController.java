package com.gt.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info(principal.toString());
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>");
        return principal;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        logger.info("-------login--------");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/login.html");
        return mv;
    }

    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public ModelAndView toLogin() {
        logger.info("-------toLogin--------");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/login.html");
        return mv;
    }
}