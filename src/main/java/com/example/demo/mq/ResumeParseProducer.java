package com.example.demo.mq;

import com.example.demo.config.RabbitMQConfig;
import com.example.demo.dto.ResumeParseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeParseProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendParseMessage(Long userId, String fileUrl, String fileName) {
        ResumeParseMessage message = new ResumeParseMessage(userId, fileUrl, fileName);
        log.info("发送简历解析消息: userId={}, fileName={}", userId, fileName);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESUME_PARSE_EXCHANGE,
                RabbitMQConfig.RESUME_PARSE_ROUTING_KEY,
                message
        );
    }
}
