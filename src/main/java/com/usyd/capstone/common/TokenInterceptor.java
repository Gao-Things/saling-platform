package com.usyd.capstone.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.usyd.capstone.common.utils.TokenUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // get request URL
        String requestURI = request.getRequestURI();

        // 解析并验证 Token
        String token = request.getHeader("Authorization"); // 从请求头获取 Token
        boolean tokenValid = TokenUtils.validateToken(token);

        if (!tokenValid) {
            // 构建错误消息对象

            ErrorResponse errorResponse = new ErrorResponse(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

            // 将对象序列化为 JSON 格式的字符串
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonErrorMessage = objectMapper.writeValueAsString(errorResponse);

            // 设置响应头和写入响应体
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(jsonErrorMessage);
        }

        return tokenValid;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 拦截处理后的操作（可选）
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后的操作（可选）
    }
}
