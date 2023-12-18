package com.vancefm.ticketstack.config;

import com.vancefm.ticketstack.logging.TicketStackRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class adds the request interceptors for Kafka
 */
@Configuration
public class TicketStackMvcConfig implements WebMvcConfigurer {

    private final TicketStackRequestInterceptor ticketStackRequestInterceptor;

    public TicketStackMvcConfig(TicketStackRequestInterceptor ticketStackRequestInterceptor) {
        this.ticketStackRequestInterceptor = ticketStackRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(ticketStackRequestInterceptor)
                .addPathPatterns("/ticket/{*path}", "/contact/{*path}");
    }
}
