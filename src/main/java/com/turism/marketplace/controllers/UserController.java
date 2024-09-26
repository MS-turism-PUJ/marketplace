package com.turism.marketplace.controllers;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.turism.marketplace.models.User;


@Controller
class UserController {
    @QueryMapping
    public User userById(@Argument String id) {
        User user = new User();
        return user;
    }
    @SchemaMapping
    public User author(User user) {
        User a = new User();
        return a;
    }
}
