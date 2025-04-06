package com.bazaar.Inventory_Tracking_System.config;

import io.github.bucket4j.Bucket;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Configuration
public class RateLimiterConfig {

    private final Map<String, Bucket> rateLimitBuckets;

    public RateLimiterConfig(Map<String, Bucket> rateLimitBuckets) {
        this.rateLimitBuckets = rateLimitBuckets;
    }

    // Register RateLimitingFilter as a bean
    @Bean
    public RateLimitingFilter rateLimitingFilter() {
        return new RateLimitingFilter(rateLimitBuckets);
    }

    // Register the filter with the servlet container for /api/* endpoints
    @Bean
    public FilterRegistrationBean<RateLimitingFilter> filterRegistrationBean(RateLimitingFilter rateLimitingFilter) {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitingFilter);
        registrationBean.addUrlPatterns("/api/*");
        return registrationBean;
    }
}
