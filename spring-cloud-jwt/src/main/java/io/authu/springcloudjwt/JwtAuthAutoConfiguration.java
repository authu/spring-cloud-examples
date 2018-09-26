package io.authu.springcloudjwt;

import org.springframework.boot.autoconfigure.cloud.CloudAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.WebFilter;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/26.
 */
@Configuration
@ConditionalOnProperty(prefix = "authu.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({JwtProperties.class})
public class JwtAuthAutoConfiguration {

    @Bean
    @ConditionalOnClass({CloudAutoConfiguration.class})
    public WebFilter webfluxJwtAutoFilter(){
        return new JwtWebFilter();
    }

    @Bean
    public JwtAuthServer jwtAuthServer(){
        return new JwtAuthServer();
    }

}
