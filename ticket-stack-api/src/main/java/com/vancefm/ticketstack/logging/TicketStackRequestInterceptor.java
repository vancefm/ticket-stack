package com.vancefm.ticketstack.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;

/**
 * This class handles the messaging for Kafka
 */
@Slf4j
@Component
public class TicketStackRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = Instant.now().toEpochMilli();
        request.setAttribute("startTime", startTime);
        log.info(request.getRemoteAddr()
                + " " + request.getMethod()
                + " " + request.getRequestURL());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long startTime = (Long) request.getAttribute("startTime");
        log.info("Response: " + HttpStatus.valueOf(response.getStatus())
                + " " + response.getContentType()
                + " " + (Instant.now().toEpochMilli() - startTime) + "ms");
    }
}
