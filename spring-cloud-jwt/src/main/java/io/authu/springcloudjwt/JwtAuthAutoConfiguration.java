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
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @ConditionalOnClass({WebFilter.class,Mono.class})
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
     * webmvc filter config
     */
    @Configuration
    @ConditionalOnClass({OncePerRequestFilter.class,FilterChain.class})
    public static class JwtPerRequestFilter extends OncePerRequestFilter {

        @Resource
        private JwtAuthServer authServer;
        @Resource
        private JwtProperties properties;

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header = request.getHeader(properties.getHeader());
            authServer.parse(header);
            filterChain.doFilter(request,response);
        }

    }

    /**
     * mvc openfeign config
     */
    @Configuration
    @ConditionalOnClass({RequestInterceptor.class,HttpServletRequest.class})
    public static class JwtMvcFeignAuthRequestInterceptor implements RequestInterceptor {

        @Resource
        private JwtProperties properties;
        @Resource
        private JwtAuthServer authServer;
        @Autowired(required = false)
        private HttpServletRequest mvcRequest;

        @Override
        public void apply(RequestTemplate requestTemplate) {
            requestTemplate.header(properties.getHeader(), mvcRequest.getHeader(properties.getHeader()));
        }

    }

    /**
     * webflux openfeign config
     *
     * fixme how to get WebFlux Request bean?
     *
     */
    @Configuration
    @ConditionalOnClass({RequestInterceptor.class,ServerRequest.class})
    public static class JwtWebFluxFeignAuthRequestInterceptor implements RequestInterceptor {

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
        }

    }

}
