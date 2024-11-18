package com.turism.marketplace.controllers;

import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import com.turism.marketplace.models.Payment;
import com.turism.marketplace.models.Service;
import com.turism.marketplace.models.ServiceCategory;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.ServiceRepository;
import com.turism.marketplace.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureGraphQlTester
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class PaymentControllerTest {

    @Autowired
    private WebGraphQlTester webGraphQlTester;

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
            null,
            null,
            null, null, null, "alimentationDrinkTest1",
            "alimentationLunchTest1",
            "alimentationDessertTest1",
            null, ServiceCategory.ALIMENTATION,
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
            null,
            null,
            null, null, null, "alimentationDrinkTest2",
            "alimentationLunchTest2",
            "alimentationDessertTest2",
            null, ServiceCategory.ALIMENTATION,
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
    void addToMyShoppingCart() {
        webGraphQlTester.mutate()
                .header("X-Preferred-Username", mockUser.getUsername())
                .build()
                .document("mutation { addToMyShoppingCart(serviceId: \"" + mockService1.getServiceId() + "\" ) }")
                .execute()
                .path("addToMyShoppingCart")
                .entity(String.class)
                .isEqualTo("Service added");

        webGraphQlTester.mutate()
                .header("X-Preferred-Username", mockUser.getUsername())
                .build()
                .document("mutation { addToMyShoppingCart(serviceId: \"" + mockService2.getServiceId() + "\" ) }")
                .execute()
                .path("addToMyShoppingCart")
                .entity(String.class)
                .isEqualTo("Service added");
    }

    @Test
    @Order(2)
    void getMyShoppingCart() {
        webGraphQlTester.mutate()
                .header("X-Preferred-Username", mockUser.getUsername())
                .build()
                .document("""
                            query {
                                getMyShoppingCart {
                                    totalAmount
                                    user {
                                        userId
                                    }
                                    paid
                                    services {
                                        name
                                    }
                                }
                            }
                        """)
                .execute()
                .path("getMyShoppingCart")
                .entity(Payment.class)
                .satisfies(payment -> {
                    assertThat(payment.getTotalAmount()).isEqualTo(mockService1.getPrice() + mockService2.getPrice());
                    assertThat(payment.getServices()).hasSize(2);
                    assertThat(payment.getServices().get(0).getName()).isEqualTo(mockService1.getName());
                    assertThat(payment.getServices().get(1).getName()).isEqualTo(mockService2.getName());
                    assertThat(payment.getPaid()).isFalse();
                    assertThat(payment.getUser().getUserId()).isEqualTo(mockUser.getUserId());
                });
    }

    @Test
    @Order(3)
    void pay() {
        webGraphQlTester.mutate()
                .header("X-Preferred-Username", mockUser.getUsername())
                .build()
                .document("""
                            mutation {
                                pay
                            }
                        """)
                .execute()
                .path("pay")
                .entity(String.class)
                .isEqualTo("Payment registered");
    }

    @Test
    @Order(4)
    void getAllMyPayments() {
        webGraphQlTester.mutate()
                .header("X-Preferred-Username", mockUser.getUsername())
                .build()
                .document("""
                            query {
                                findAllMyPayments {
                                    totalAmount
                                    user {
                                        userId
                                    }
                                    paid
                                    services {
                                        name
                                    }
                                }
                            }
                        """).execute()
                .path("findAllMyPayments")
                .entityList(Payment.class)
                .hasSize(1)
                .satisfies(payments -> {
                    Payment payment = payments.get(0);
                    assertThat(payment.getTotalAmount()).isEqualTo(mockService1.getPrice() + mockService2.getPrice());
                    assertThat(payment.getServices()).hasSize(2);
                    assertThat(payment.getServices().get(0).getName()).isEqualTo(mockService1.getName());
                    assertThat(payment.getServices().get(1).getName()).isEqualTo(mockService2.getName());
                    assertThat(payment.getPaid()).isTrue();
                    assertThat(payment.getUser().getUserId()).isEqualTo(mockUser.getUserId());
                });
    }

}
