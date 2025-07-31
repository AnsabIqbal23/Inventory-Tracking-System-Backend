package com.bazaar.Inventory_Tracking_System.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import java.io.IOException;
import java.util.Map;

public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> rateLimitBuckets;

    public RateLimitingFilter(Map<String, Bucket> rateLimitBuckets) {
        this.rateLimitBuckets = rateLimitBuckets;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Skip rate limiting for OPTIONS requests (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }

        String clientIP = httpRequest.getRemoteAddr();

        // Get or create a new bucket for this client IP
        Bucket bucket = rateLimitBuckets.computeIfAbsent(clientIP, k -> Bucket4jConfiguration.createNewBucket());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            // Return proper HTTP status instead of throwing ServletException
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // 429
            httpResponse.setContentType("application/json");
            httpResponse.getWriter().write("{\"error\":\"Too many requests! Please try again later.\"}");
        }
    }
}