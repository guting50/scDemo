package com.gt.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;
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

                String accessToken = exchange.getRequest().getQueryParams().getFirst("accessToken");
                MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
                if (null != cookies) {
                    for (String key : cookies.keySet()) {
                        if ("accessToken".equals(key)) {
                            accessToken = cookies.getFirst(key).getValue();
                        }
                    }
                }

                if (StringUtils.isEmpty(accessToken)) {
                    accessToken = "";
                }
                String redirectUrl = ssoFeign.hasKeyAndRedirect(accessToken);
                //访问路径
                String url = ((LinkedHashSet) exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)).toArray()[0].toString();
                if (!StringUtils.isEmpty(redirectUrl)) {
                    StringBuilder sb = new StringBuilder(url)
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
