package com.turism.marketplace.controllers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.ServiceCategory;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.ServiceRepository;
import com.turism.marketplace.repositories.UserRepository;

@SpringBootTest
@AutoConfigureGraphQlTester
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class ServiceControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    static final User mockUser = new User(
            "userIdTest",
            "userNameTestuserUsernameTest",
            "userNameTest",
            "userEmailTest");

    static final Service mockService1 = new Service(
            "alimentationIdTest1",
            10.0f,
            "alimentationNameTest1",
            "alimentationCityTest1",
            "alimentationCountryTest1",
            "alimentationDescriptionTest1",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "alimentationDrinkTest1",
            "alimentationLunchTest1",
            "alimentationDessertTest1",
            ServiceCategory.ALIMENTATION,
            null,
            mockUser);

    static final Service mockService2 = new Service(
            "alimentationIdTest2",
            10.0f,
            "alimentationNameTest2",
            "alimentationCityTest2",
            "alimentationCountryTest2",
            "alimentationDescriptionTest2",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "alimentationDrinkTest2",
            "alimentationLunchTest2",
            "alimentationDessertTest2",
            ServiceCategory.ALIMENTATION,
            null,
            mockUser);

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository, @Autowired ServiceRepository serviceRepository) {
        postgres.start();

        userRepository.save(mockUser);
        serviceRepository.save(mockService1);
        serviceRepository.save(mockService2);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void getAllServices() {
        graphQlTester.document("""
                    query {
                        findAllServices(page: 1, limit: 10) {
                            serviceId
                            price
                            name
                            city
                            country
                            description
                            drink
                            lunch
                            dessert
                            category
                            user {
                                userId
                                name
                                email
                                username
                            }
                        }
                    }
                """)
                .execute()
                .path("findAllServices")
                .entityList(Service.class)
                .hasSize(2)
                .satisfies(services -> {
                    assertThat(services.get(0).getName()).isEqualTo(mockService1.getName());
                    assertThat(services.get(1).getName()).isEqualTo(mockService2.getName());
                });
    }

    @Test
    @Order(1)
    void getServiceById() {
        graphQlTester.document("""
                    query {
                        findServiceById(serviceId: "alimentationIdTest1") {
                            serviceId
                            price
                            name
                            city
                            country
                            description
                            drink
                            lunch
                            dessert
                            category
                            user {
                                userId
                                name
                                email
                                username
                            }
                        }
                    }
                """)
                .execute()
                .path("findServiceById")
                .entity(Service.class)
                .satisfies(service -> {
                    assertThat(service.getName()).isEqualTo(mockService1.getName());
                });
    }

    @Test
    @Order(1)
    void getServicesByFilter() {
        graphQlTester.document("""
                    query {
                        findServicesByFilter(filter: {filter: "alimentationNameTest1", price: {moreThan: 5.0, lessThan: 15.0}, categories: [ALIMENTATION]}, page: 1, limit: 10) {
                            serviceId
                            price
                            name
                            city
                            country
                            description
                            drink
                            lunch
                            dessert
                            category
                            user {
                                userId
                                name
                                email
                                username
                            }
                        }
                    }
                """)
                .execute()
                .path("findServicesByFilter")
                .entityList(Service.class)
                .hasSize(1)
                .satisfies(services -> {
                    assertThat(services.get(0).getName()).isEqualTo(mockService1.getName());
                });
    }

}
