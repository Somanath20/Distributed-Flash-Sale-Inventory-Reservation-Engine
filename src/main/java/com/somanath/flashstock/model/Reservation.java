package com.somanath.flashstock.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name="reservations", indexes={@Index(name="idx_reservation_status_expiry", columnList="status,expiresAt")})
public class Reservation {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true) private String reservationKey;
    @Column(nullable=false) private String sku;
    @Column(nullable=false) private int quantity;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status;
    @Column(nullable=false) private Instant expiresAt;
    protected Reservation() {}
    public Reservation(String key,String sku,int quantity,Instant expiresAt){this.reservationKey=key;this.sku=sku;this.quantity=quantity;this.expiresAt=expiresAt;this.status=Status.ACTIVE;}
    public enum Status { ACTIVE, CONFIRMED, EXPIRED }
    public Long getId(){return id;} public String getReservationKey(){return reservationKey;} public String getSku(){return sku;} public int getQuantity(){return quantity;} public Status getStatus(){return status;} public Instant getExpiresAt(){return expiresAt;}
    public void confirm(){status=Status.CONFIRMED;} public void expire(){status=Status.EXPIRED;}
}
