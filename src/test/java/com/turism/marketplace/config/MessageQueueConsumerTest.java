package com.turism.marketplace.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import com.google.gson.Gson;
import com.turism.marketplace.dtos.ContentMessageDTO;
import com.turism.marketplace.dtos.ServiceMessageDTO;
import com.turism.marketplace.dtos.UserMessageDTO;
import com.turism.marketplace.models.Content;
import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.ServiceCategory;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.ContentRepository;
import com.turism.marketplace.repositories.ServiceRepository;
import com.turism.marketplace.repositories.UserRepository;

@SpringBootTest
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
class MessageQueueConsumerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    private static KafkaTemplate<String, Object> kafkaTemplate;

    static void createKafkaProducer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);

        configProps.put(ProducerConfig.ACKS_CONFIG, "1");

        ProducerFactory<String, Object> producerFactory = new DefaultKafkaProducerFactory<>(configProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    static final UserMessageDTO userMessageDTO = new UserMessageDTO("userIdTest", "usernameTest", "nameTest",
            "emailTest");

    static final ServiceMessageDTO serviceMessageDTO = new ServiceMessageDTO(
            "alimentationIdTest",
            10.0f,
            "alimentationNameTest",
            "alimentationCityTest",
            "alimentationCountryTest",
            "alimentationDescriptionTest",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "alimentationDrinkTest",
            "alimentationLunchTest",
            "alimentationDessertTest",
            ServiceCategory.ALIMENTATION,
            "userIdTest");
    
    static final ContentMessageDTO contentMessageDTO = new ContentMessageDTO(
            "contentIdTest",
            "contentNameTest",
            "contentDescriptionTest",
            "contentLinkTest",
            "alimentationIdTest",
            "userIdTest");

    @BeforeAll
    static void setup() {
        postgres.start();
        kafka.start();

        createKafkaProducer();

        Gson gson = new Gson();
        kafkaTemplate.send("usersQueue", gson.toJson(userMessageDTO));
        kafkaTemplate.send("servicesQueue", gson.toJson(serviceMessageDTO));
        kafkaTemplate.send("contentsQueue", gson.toJson(contentMessageDTO));
    }

    @AfterAll
    static void teardown() {
        kafka.stop();
        postgres.stop();
    }

    @Test
    @Order(1)
    void createUser() {
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    User createdMockUser = userRepository.findByUsername(
                            userMessageDTO.getUsername());
                    assertThat(createdMockUser.getName()).isEqualTo(userMessageDTO.getName());
                    assertThat(createdMockUser.getEmail()).isEqualTo(userMessageDTO.getEmail());
                    assertThat(createdMockUser.getUserId()).isEqualTo(userMessageDTO.getUserId());
                });
    }

    @Test
    @Order(1)
    void createService() {
        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                Service createdMockService = serviceRepository.findById(
                        serviceMessageDTO.getServiceId()).get();
                assertThat(createdMockService.getName()).isEqualTo(serviceMessageDTO.getName());
                assertThat(createdMockService.getCity()).isEqualTo(serviceMessageDTO.getCity());
                assertThat(createdMockService.getCountry()).isEqualTo(serviceMessageDTO.getCountry());
                assertThat(createdMockService.getDescription()).isEqualTo(serviceMessageDTO.getDescription());
                assertThat(createdMockService.getDrink()).isEqualTo(serviceMessageDTO.getDrink());
                assertThat(createdMockService.getLunch()).isEqualTo(serviceMessageDTO.getLunch());
                assertThat(createdMockService.getDessert()).isEqualTo(serviceMessageDTO.getDessert());
                assertThat(createdMockService.getCategory()).isEqualTo(serviceMessageDTO.getCategory());
            });
    }

    @Test
    @Order(1)
    void createContent() {
        await()
            .pollInterval(Duration.ofSeconds(3))
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                Content createdMockContent = contentRepository.findById(
                        contentMessageDTO.getContentId()).get();
                assertThat(createdMockContent.getName()).isEqualTo(contentMessageDTO.getName());
                assertThat(createdMockContent.getDescription()).isEqualTo(contentMessageDTO.getDescription());
                assertThat(createdMockContent.getLink()).isEqualTo(contentMessageDTO.getLink());
                assertThat(createdMockContent.getService().getServiceId()).isEqualTo(contentMessageDTO.getServiceId());
                assertThat(createdMockContent.getUser().getUserId()).isEqualTo(contentMessageDTO.getUserId());
            });
    }
}
