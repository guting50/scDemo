package com.gt.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//
//        String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("123456");
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
////        manager.createUser(User.withUsername("user_1").password(finalPassword).authorities("USER").build());
////        manager.createUser(User.withUsername("user_2").password(finalPassword).authorities("USER").build());
//        manager.createUser(User.withUsername("user_1").password("123456").authorities("USER").build());
//        manager.createUser(User.withUsername("user_2").password("123456").authorities("USER").build());
//
//        return manager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    UserDetailsService customUserService() {
        return new UserServiceImpl();
    }

    /**
     * 认证管理器配置
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * httpSecurity中配置所有请求的安全验证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //开启登录配置
                .authorizeRequests()
                // 放行
                .antMatchers("/oauth/**")
                .permitAll()
                //表示剩余的其他接口，登录之后就能访问
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //定义登录页面，未登录时，访问一个需要登录之后才能访问的接口，会自动跳转到该页面
                .loginPage("/testpage.html")
                //登录处理接口
                .loginProcessingUrl("/users/doLogin")
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .csrf().disable()
        ;

//        http.requestMatchers()
//                // 其他请求必须认证通过
//                .anyRequest()
//                .and()
//                //开启登录配置
//                .authorizeRequests()
//                // 放行
//                .antMatchers("/oauth/**")
//                .permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/users/login")
//                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/users/toLogin");
    }
}