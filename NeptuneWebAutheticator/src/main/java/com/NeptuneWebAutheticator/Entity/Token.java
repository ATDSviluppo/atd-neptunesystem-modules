package com.NeptuneWebAutheticator.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long tokenId;

    @Column(name = "token")
    private String token;

    @Column(name = "expirationDate")
    private Date expirationDate;

    @Column(name = "tenantId")
    private Long tenantId;
}
