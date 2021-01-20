package com.gt.feign.hystrix;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 默认fallback，减少必要的编写fallback类
 *
 * @param <T>
 */
@Component
public class MyFallbackFactory<T> implements FallbackFactory<T> {

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable cause) {
        return (T) ("sorry sayHello1 " + cause.getMessage());
    }
}