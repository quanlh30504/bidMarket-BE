package com.example.bidMarket.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "full_name", length = 255)
    private String fullName;

    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

}
