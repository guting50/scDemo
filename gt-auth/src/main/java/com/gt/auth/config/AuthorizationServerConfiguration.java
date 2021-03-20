package com.gt.auth.config;

import com.gt.auth.controller.UserController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    DataSource dataSource;


    /**
     * 用来配置客户端详情服务
     * （ClientDetailsService），客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息；
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //默认值InMemoryTokenStore对于单个服务器是完全正常的（即，在发生故障的情况下，低流量和热备份备份服务器）。
        // 大多数项目可以从这里开始，也可以在开发模式下运行，以便轻松启动没有依赖关系的服务器。
        //这JdbcTokenStore是同一件事的JDBC版本，它将令牌数据存储在关系数据库中。如果您可以在服务器之间共享数据库，则可以使用JDBC版本，
        // 如果只有一个，则扩展同一服务器的实例，或者如果有多个组件，则授权和资源服务器。要使用JdbcTokenStore你需要“spring-jdbc”的类路径。

        //这个地方指的是从jdbc查出数据来存储
        clients.withClientDetails(clientDetails());

//        String finalSecret = "{bcrypt}" + new BCryptPasswordEncoder().encode("123456");
//
//        // 配置两个客户端，一个用于password认证一个用于client认证
//        clients.inMemory() //将客户端的信息存储在内存中
//                .withClient("client_1") //创建了一个client名为client_1的客户端
////                .resourceIds(Utils.RESOURCEIDS.ORDER)
//                .authorizedGrantTypes("client_credentials", "refresh_token") //配置验证类型
//                .scopes("select") //配置客户端域为“select”
//                .authorities("oauth2")
//                .secret(finalSecret)//客户端密码
//                .and()
//                .withClient("client_2")
////                .resourceIds(Utils.RESOURCEIDS.ORDER)
//                .authorizedGrantTypes("password", "refresh_token")
//                .scopes("server")
//                .authorities("oauth2")
//                .secret(finalSecret);
    }

    /**
     * ClientDetails实现
     *
     * @return
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 加入对授权码模式的支持
     *
     * @param dataSource
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 用来配置授权
     * （authorization）以及令牌（token）的访问端点和令牌服务(token services)，还有token的存储方式(tokenStore)；
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore())// 令牌管理服务
                .authenticationManager(authenticationManager)// 认证管理器
//                .userDetailsService(clientDetails())
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .setClientDetailsService(clientDetails());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource); //存储到db中
//        return new InMemoryTokenStore(); //存储内存中
//        return new RedisTokenStore(redisConnectionFactory); //存储到redis中
//        return new MyRedisTokenStore(redisConnectionFactory);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束；
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 允许表单认证
        security
                .tokenKeyAccess("permitAll()")//获取token请求不进行拦截
                .checkTokenAccess("isAuthenticated()") //验证通过返回token信息
                .allowFormAuthenticationForClients();//允许客户端使用clieng_id和clieng_secret获取token
    }
}
