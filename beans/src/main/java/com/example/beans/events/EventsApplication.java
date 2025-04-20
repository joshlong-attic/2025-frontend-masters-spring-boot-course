package com.example.beans.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class EventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventsApplication.class, args);
    }

    private final AtomicInteger counter = new AtomicInteger();

    int counter (){ return counter.get(); }

    @Bean
    ApplicationListener<ApplicationReadyEvent> listener(ApplicationEventPublisher publisher) {
        return event -> publisher
                .publishEvent(new SpringIsAwesomeEvent());
    }

    @EventListener
    void on(ApplicationReadyEvent event) {
        register(event);
    }

    @EventListener
    void on(ContextRefreshedEvent event) {
        register(event);
    }

    @EventListener
    void on(SpringIsAwesomeEvent event) {
        register(event);
    }

    void register(Object event) {
        System.out.println("Registering [" + event + +']');
        this.counter.incrementAndGet();
    }
}

record SpringIsAwesomeEvent() {
}