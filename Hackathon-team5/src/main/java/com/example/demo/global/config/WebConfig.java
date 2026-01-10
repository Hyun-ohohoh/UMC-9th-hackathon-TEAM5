package com.example.demo.global.config;

import com.example.demo.global.security.interceptor.JwtInterceptor;
import com.example.demo.global.security.resolver.AuthUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.example.demo.global.intercepter.LoggingInterceptor;

// Interceptor를 Spring MVC에 등록, 특정 URL 경로에 대해 적용
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;
    private final JwtInterceptor jwtInterceptor;
    private final AuthUserArgumentResolver authUserArgumentResolver;

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor)
                // 인터셉터가 실행될 경로를 설정하는 필터
                .addPathPatterns("/**")
                // 인터셉터가 실행되지 않을 경로를 설정하는 필터
                .excludePathPatterns("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**");

        // JWT 인터셉터
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(
                        "/api/v1/auth/**",
                        "/api/v1/user/session",
                        "/h2-console/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/api/v1/v3/api-docs/**"
                );
    }

    @Override
    public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
        // 모든 API 경로에 /api/v1 접두사 추가
        configurer.addPathPrefix("/api/v1", c -> c.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class));
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // 전역 CORS 설정
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        // 비동기 요청 타임아웃 설정 (5분)
        configurer.setDefaultTimeout(300_000L);
        Executor executor = Executors.newCachedThreadPool();
        Executor securityContextExecutor = new DelegatingSecurityContextExecutor(executor);
        AsyncTaskExecutor asyncTaskExecutor = new TaskExecutorAdapter(securityContextExecutor);
        configurer.setTaskExecutor(asyncTaskExecutor);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authUserArgumentResolver);
    }
}