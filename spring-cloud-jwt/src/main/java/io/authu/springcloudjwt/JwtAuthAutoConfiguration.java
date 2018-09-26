package io.authu.springcloudjwt;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/26.
 */
@Configuration
@ConditionalOnProperty(prefix = "authu.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({JwtProperties.class})
public class JwtAuthAutoConfiguration {

    @Bean
    public JwtAuthServer jwtAuthServer(){
        return new JwtAuthServer();
    }

    /**
     * webflux filter config
     */
    @Configuration
    @ConditionalOnClass({WebFilter.class})
    public static class JwtWebFilter implements WebFilter {

        @Resource
        private JwtAuthServer authServer;

        @Override
        public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String token = authServer.getToken(request);
            authServer.parse(token);

            return chain.filter(exchange);
        }
    }

    /**
     * openfeign config
     *
     * fixme how to get Request bean?
     *
     */
    @Configuration
    @ConditionalOnClass({RequestInterceptor.class})
    public static class JwtFeignAuthRequestInterceptor implements RequestInterceptor {

        @Resource
        private JwtProperties properties;
        @Resource
        private JwtAuthServer authServer;
        @Autowired(required = false)
        private ServerHttpRequest webfluxRequest;
        @Autowired(required = false)
        private ServerRequest serverRequest;

        @Override
        public void apply(RequestTemplate requestTemplate) {

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            requestTemplate.header(properties.getHeader(), authServer.getToken(webfluxRequest));
        }

    }

}
