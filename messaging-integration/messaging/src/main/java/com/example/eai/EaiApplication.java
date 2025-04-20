package com.example.eai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.core.PulsarTemplate;

@SpringBootApplication
public class EaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(EaiApplication.class, args);
    }

}

@Configuration
class RabbitConfiguration {

    static final String DESTINATION = "rabbit-adoptions";

    @Bean
    Binding adoptionsBinding(Queue adoptionsQueue, Exchange adoptionsExchange) {
        return BindingBuilder
                .bind(adoptionsQueue)
                .to(adoptionsExchange)
                .with(DESTINATION)
                .noargs();
    }

    @Bean
    Queue adoptionsQueue() {
        return QueueBuilder
                .durable(DESTINATION)
                .build();
    }

    @Bean
    Exchange adoptionsExchange() {
        return ExchangeBuilder
                .directExchange(DESTINATION)
                .build();
    }

    @RabbitListener(queues = DESTINATION)
    void on(Message<AdoptionRequest> ar) {
        Listener.register(DESTINATION, ar);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper,
                EaiApplication.class.getPackage().getName());
    }

    @Bean
    ApplicationRunner rabbitRunner(RabbitTemplate rt) {
        return _ -> rt
                .convertAndSend(DESTINATION, new AdoptionRequest("Prancer"));
    }
}

@Configuration
class PulsarConfiguration {

    static final String TOPIC = "pulsar-adoptions";

    @Bean
    ApplicationRunner pulsarRunner(PulsarTemplate<AdoptionRequest> pulsarTemplate) {
        return args -> pulsarTemplate.send(TOPIC, new AdoptionRequest("Prancer"));
    }

    @PulsarListener(topics = TOPIC, subscriptionName = "adoptions-subscription")
    void on(Message<AdoptionRequest> ar) {
        Listener.register(TOPIC, ar);
    }
}

@Configuration
class KafkaConfiguration {

    static final String DESTINATION = "kafka-adoptions";

    @Bean
    ApplicationRunner kafkaRunner(KafkaTemplate<String, AdoptionRequest> kafkaTemplate) {
        return _ -> kafkaTemplate.send(DESTINATION, new AdoptionRequest("Prancer"));
    }

    @KafkaListener(topics = DESTINATION, groupId = "adoptions-group")
    void on(Message<AdoptionRequest> ar) {
        Listener.register(DESTINATION, ar);
    }

}

abstract class Listener {

    static void register(String name, Message<?> message) {
        System.out.println("------");
        System.out.println("Received: " + name + ": [" + message.getPayload() + ']');
        message.getHeaders().forEach((k, v) -> System.out.println(k + ": " + v));
    }

}

record AdoptionRequest(String name) {
}