package com.turism.marketplace.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contents")
public class Content {
    @Id
    private String contentId;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String link;

    @ManyToOne
    @JoinColumn(name = "serviceId")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Content(String contentId) {
        this.contentId = contentId;
    }
}
