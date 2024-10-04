package com.turism.marketplace.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table  (name = "users")
public class User {
    @Id
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @OneToMany (mappedBy = "contentId")
    private List<Content> contents;
    @OneToMany (mappedBy = "paymentId")
    private List<Payment> payments;
    
    public User(String userId, String username, String name, String email) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.email = email;
    }
}
