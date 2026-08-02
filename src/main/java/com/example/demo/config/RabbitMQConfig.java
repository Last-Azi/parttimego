package com.example.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RESUME_PARSE_QUEUE = "resume.parse.queue";
    public static final String RESUME_PARSE_EXCHANGE = "resume.parse.exchange";
    public static final String RESUME_PARSE_ROUTING_KEY = "resume.parse";

    @Bean
    public Queue resumeParseQueue() {
        return QueueBuilder.durable(RESUME_PARSE_QUEUE).build();
    }

    @Bean
    public DirectExchange resumeParseExchange() {
        return new DirectExchange(RESUME_PARSE_EXCHANGE);
    }

    @Bean
    public Binding resumeParseBinding(Queue resumeParseQueue, DirectExchange resumeParseExchange) {
        return BindingBuilder.bind(resumeParseQueue).to(resumeParseExchange).with(RESUME_PARSE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
