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

import com.turism.marketplace.models.Content;
import com.turism.marketplace.models.User;
import com.turism.marketplace.repositories.ContentRepository;
import com.turism.marketplace.repositories.UserRepository;

@SpringBootTest
@AutoConfigureGraphQlTester
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class ContentControllerTest {

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

    static final Content mockContent1 = new Content(
            "contentIdTest1",
            "contentNameTest1",
            "contentDescriptionTest1",
            "contentLinkTest1",
            null,
            mockUser);
    
    static final Content mockContent2 = new Content(
            "contentIdTest2",
            "contentNameTest2",
            "contentDescriptionTest2",
            "contentLinkTest2",
            null,
            mockUser);

    @BeforeAll
    static void beforeAll(@Autowired UserRepository userRepository, @Autowired ContentRepository contentRepository) {
        postgres.start();

        userRepository.save(mockUser);
        contentRepository.save(mockContent1);
        contentRepository.save(mockContent2);
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    @Order(1)
    void getAllContents() {
        graphQlTester.document("""
            query {
                findAllContents(page: 1, limit: 10) {
                    contentId
                    name
                    description
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
        .path("findAllContents")
        .entityList(Content.class)
        .hasSize(2)
        .satisfies(contents -> {
            assertThat(contents.get(0).getName()).isEqualTo(mockContent1.getName());
            assertThat(contents.get(1).getName()).isEqualTo(mockContent2.getName());
        });
    }

    @Test
    @Order(1)
    void getContentById() {
        graphQlTester.document("""
            query {
                findContentById(contentId: "contentIdTest1") {
                    contentId
                    name
                    description
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
        .path("findContentById")
        .entity(Content.class)
        .satisfies(content -> {
            assertThat(content.getName()).isEqualTo(mockContent1.getName());
        });
    }

    @Test
    @Order(1)
    void getContentsByFilter() {
        graphQlTester.document("""
            query {
                findContentsByFilter(filter: "contentNameTest2", page: 1, limit: 10) {
                    contentId
                    name
                    description
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
        .path("findContentsByFilter")
        .entityList(Content.class)
        .hasSize(1)
        .satisfies(contents -> {
            assertThat(contents.get(0).getName()).isEqualTo(mockContent2.getName());
        });
    }
    
}
