package com.turism.marketplace.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table  (name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String contentId;
    private String photo;
    @Column(nullable = false)
    private String name;
    private String description;
    private String link;
    private String category;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @OneToOne
    @JoinColumn(name = "serviceId")
    private Service service;
}
