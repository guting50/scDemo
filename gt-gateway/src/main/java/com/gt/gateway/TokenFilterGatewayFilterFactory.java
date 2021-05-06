package com.gt.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenFilterGatewayFilterFactory.Config> {

    @Autowired
    private SsoFeign ssoFeign;

    private static final Logger log = LoggerFactory.getLogger(TokenFilterGatewayFilterFactory.class);
    private static final String KEY = "withParams";

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList(KEY);
    }

    public TokenFilterGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (config.isWithParams()) {
//                String token = exchange.getRequest().getQueryParams().getFirst("accessToken");
//                if (token == null || token.isEmpty()) {
//                    StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
//                            .append(": accessToken is empty...");
//                    log.info(sb.toString());
//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//                    return exchange.getResponse().setComplete();
//                }

                //访问路径
                String url = exchange.getRequest().getURI().toString();
                String redirectUrl = ssoFeign.hasKeyAndRedirect("accessToken");
                if (!StringUtils.isEmpty(redirectUrl)) {
                    StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                            .append(": accessToken is empty...");
                    log.info(sb.toString());

//                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().set("Location", redirectUrl + url);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    public static class Config {

        private boolean withParams;

        public boolean isWithParams() {
            return withParams;
        }

        public void setWithParams(boolean withParams) {
            this.withParams = withParams;
        }

    }
}
