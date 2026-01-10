package com.example.demo.global.security.interceptor;

import com.example.demo.global.exception.BusinessException;
import com.example.demo.global.exception.ErrorCode;
import com.example.demo.global.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object Handler) {

        // CORS preflight 요청(OPTIONS)은 통과시킴
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")){
            throw new BusinessException(ErrorCode.AUTHENTICATION_REQUIRED);
        }

        // 'Bearer '를 제외한 실제 토큰 값만 추출
        String token = header.substring(7);

        if(!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        String userId = jwtTokenProvider.getPayload(token);
        request.setAttribute("userId", Long.parseLong(userId));

        return true;

    }
}
