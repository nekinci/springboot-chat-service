package com.spotify.api.service;

import com.spotify.api.config.RabbitMQConfig;
import com.spotify.api.core.abstraction.MQService;
import com.spotify.api.dto.MailDto;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqServiceImpl implements MQService {
    @Autowired
    AmqpTemplate rabbitMqTemplate;
    @Autowired
    private RabbitMQConfig config;
    @Override
    public void sendMail(MailDto mail) {
        rabbitMqTemplate.convertAndSend(config.getExchange(), config.getRoutingKey(), mail);
    }
}
