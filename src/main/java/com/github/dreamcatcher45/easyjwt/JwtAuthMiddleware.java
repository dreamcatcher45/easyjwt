package com.github.dreamcatcher45.easyjwt;

import com.github.dreamcatcher45.easyjwt.config.JwtProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthMiddleware implements HandlerInterceptor, WebMvcConfigurer {
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final ObjectMapper mapper;
    private final List<String> excludePatterns;
    private final List<String> includePatterns;

    public JwtAuthMiddleware(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
        this.mapper = new ObjectMapper();
        this.excludePatterns = new ArrayList<>();
        this.includePatterns = new ArrayList<>();
    }

    public JwtAuthMiddleware addExcludePatterns(String... patterns) {
        excludePatterns.addAll(List.of(patterns));
        return this;
    }

    public JwtAuthMiddleware addIncludePatterns(String... patterns) {
        includePatterns.addAll(List.of(patterns));
        return this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = extractToken(request);
        if (token == null) {
            return sendError(response, 401, "No token provided");
        }
        return jwtUtil.validateToken(token) || sendError(response, 401, "Invalid token");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtProperties.getHeaderName());
        if (bearerToken != null && bearerToken.startsWith(jwtProperties.getTokenPrefix())) {
            return bearerToken.substring(jwtProperties.getTokenPrefix().length());
        }
        return null;
    }

    private boolean sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), Map.of(
            "status", status,
            "message", message
        ));
        return false;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        var interceptor = registry.addInterceptor(this);
        
        if (!includePatterns.isEmpty()) {
            interceptor.addPathPatterns(includePatterns);
        }
        
        if (!excludePatterns.isEmpty()) {
            interceptor.excludePathPatterns(excludePatterns);
        }
    }
}