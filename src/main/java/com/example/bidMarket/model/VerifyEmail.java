package com.example.bidMarket.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class VerifyEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer veid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    @OneToOne
    private User user;
}
