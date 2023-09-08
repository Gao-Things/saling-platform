package com.usyd.capstone.common.compents;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
public class RequestRateLimitInterceptor implements HandlerInterceptor {

    private final int maxRequestsPerMinute = 2; // 最大请求数
    private final ConcurrentHashMap<String, ArrayList<Long>> ipRequestTimestamps = new ConcurrentHashMap<>();
    private final long requestIntervalMillis = TimeUnit.SECONDS.toMillis(10); //每个间隔为10秒

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = getClientIp(request);

        long currentTime = System.currentTimeMillis();
        ArrayList<Long> temp = ipRequestTimestamps.get(clientIp);
        Long firstRequestTimeInLastRequestInterval = null;
        if(temp != null) {
            int tempSize = temp.size();
            if (tempSize > 0)
                firstRequestTimeInLastRequestInterval = temp.get(0);
        }

        // 测试不方便注释一下
        return true;
//        if (firstRequestTimeInLastRequestInterval == null || currentTime - firstRequestTimeInLastRequestInterval >= requestIntervalMillis) {
//            // 请求时间间隔超过限制，允许请求
//            temp = new ArrayList<>();
//            temp.add(currentTime);
//            ipRequestTimestamps.put(clientIp, temp);
//            return true;
//        } else if (temp.size() < maxRequestsPerMinute) {
//            // 请求速率未超限，允许请求
//            temp.add(currentTime);
//            return true;
//        } else {
//            // 请求速率超限，返回错误响应
//            response.setStatus(429);
//            response.getWriter().write("Too many requests from this IP. Please try again later.");
//            return false;
//        }
    }

    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }
}
