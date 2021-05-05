package com.gt.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TokenFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<TokenFilterGatewayFilterFactory.Config> {


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
                String token = exchange.getRequest().getQueryParams().getFirst("token");
                if (token == null || token.isEmpty()) {
                    StringBuilder sb = new StringBuilder(exchange.getRequest().getURI().getRawPath())
                            .append(": token is empty...");
                    log.info(sb.toString());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            return chain.filter(exchange);
        };
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
