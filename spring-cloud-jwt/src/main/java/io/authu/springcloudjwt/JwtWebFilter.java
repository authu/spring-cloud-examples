package io.authu.springcloudjwt;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/26.
 */
@Slf4j
public class JwtWebFilter implements WebFilter {

    @Resource
    private JwtProperties properties;
    @Resource
    private JwtAuthServer authServer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        List<String> list = request.getHeaders().get(properties.getHeader());
        if (list == null || list.size() != 1) {
            log.warn("Token is empty!");
            throw new UnsupportedJwtException("Token is empty!");
        }

        authServer.parse(list.get(0));

        return chain.filter(exchange);
    }
}
