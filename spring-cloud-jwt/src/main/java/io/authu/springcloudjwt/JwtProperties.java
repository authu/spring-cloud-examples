package io.authu.springcloudjwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Created by MrTT (jiang.taojie@foxmail.com)
 * 2018/9/26.
 */
@Data
@ConfigurationProperties(prefix = "authu.jwt")
public class JwtProperties {
    private boolean enable = true;
    private String header = "Authorization";
    private String prefix = "Bearer ";
    private String secret = "secret";
    private Duration timeout = Duration.ofDays(1);
}
