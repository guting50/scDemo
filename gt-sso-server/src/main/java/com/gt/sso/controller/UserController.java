package com.gt.sso.controller;

import com.gt.sso.service.UserService;
import com.gt.sso.vo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @ Description：
 */

@RestController
public class UserController {
    @Autowired
    private StringRedisTemplate template;
    @Autowired
    UserService userService;

    /**
     * 判断key是否存在
     */
    @RequestMapping("/redis/hasKey/{key}")
    public Boolean hasKey(@PathVariable("key") String key) {
        try {
            return template.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/redis/hasKeyAndRedirect")
    public String hasKeyAndRedirect(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "key") String key) {
        try {
            if (!template.hasKey(key)) {
//                String host = request.getHeader("host");
                String host = request.getScheme() + "://" + request.getRemoteHost() + ":" + request.getServerPort();
                return host + "/sso/loginPage?url=";
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 校验用户名密码，成功则返回通行令牌（这里写死huanzi/123456）
     */
    @RequestMapping("/sso/checkUsernameAndPassword")
    private String checkUsernameAndPassword(String username, String password) {
        //通行令牌
        String flag = null;
        User user = userService.login(username);

        if (user != null && user.getPassword().equals(password)) {
            //用户名+时间戳（这里只是demo，正常项目的令牌应该要更为复杂）
            flag = username + System.currentTimeMillis();
            //令牌作为key，存用户id作为value（或者直接存储可暴露的部分用户信息也行）设置过期时间（我这里设置3分钟）
            template.opsForValue().set(flag, "1", (long) (3 * 60), TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * 跳转登录页面
     */
    @RequestMapping("/sso/loginPage")
    private ModelAndView loginPage(String url) {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("url", url);
        return modelAndView;
    }

    /**
     * 页面登录
     */
    @RequestMapping("/sso/login")
    private String login(HttpServletResponse response, String username, String password, String url) {
        String check = checkUsernameAndPassword(username, password);
        if (!StringUtils.isEmpty(check)) {
            try {
                Cookie cookie = new Cookie("accessToken", check);
                cookie.setMaxAge(60 * 3);
                //设置域
//                cookie.setDomain("huanzi.cn");
                //设置访问路径
                cookie.setPath("/");
                response.addCookie(cookie);
                //重定向到原先访问的页面
                response.sendRedirect(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        return "登录失败";
    }

}
