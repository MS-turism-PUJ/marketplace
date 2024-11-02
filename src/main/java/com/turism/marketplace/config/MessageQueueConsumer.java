package com.turism.marketplace.config;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.turism.marketplace.services.ServiceService;
import com.turism.marketplace.services.UserService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.turism.marketplace.dtos.ServiceMessageDTO;
import com.turism.marketplace.dtos.UserMessageDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MessageQueueConsumer {
    private final String usersQueueName = "usersQueue";
    private final String servicesQueueName = "servicesQueue";

    private final UserService userService;
    private final ServiceService serviceService;

    @Autowired
    public MessageQueueConsumer(UserService userService, ServiceService serviceService) {
        this.userService = userService;
        this.serviceService = serviceService;
    }

    @Bean
    public ConsumerFactory<String, UserMessageDTO> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put("auto.offset.reset", "earliest");
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "marketplace-group");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.turism.*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserMessageDTO>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserMessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @KafkaListener(topics = usersQueueName, groupId = "marketplace-group")
    public void listenUsers(String userJson) {
        log.info("Received UserMessageDTO: {}", userJson);
        Gson gson = new Gson();
        UserMessageDTO user = gson.fromJson(userJson, UserMessageDTO.class);
        userService.createUser(user.toUser());
    }

    @KafkaListener(topics = servicesQueueName, groupId = "marketplace-group")
    public void listenServices(String serviceJson) {
        log.info("Received ServiceMessageDTO: {}", serviceJson);
        Gson gson = new Gson();
        ServiceMessageDTO service = gson.fromJson(serviceJson, ServiceMessageDTO.class);
        try {
            serviceService.createService(service.toService());
        } catch (ParseException e) {
            log.error("Error parsing service date", e);
        }
    }
}