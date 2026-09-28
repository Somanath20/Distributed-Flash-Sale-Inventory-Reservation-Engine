package com.somanath.flashstock.model;

import jakarta.persistence.*;

@Entity
@Table(name="products")
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, unique=true) private String sku;
    @Column(nullable=false) private String name;
    @Column(nullable=false) private int stock;
    @Version private long version;
    protected Product() {}
    public Product(String sku, String name, int stock) { this.sku=sku; this.name=name; this.stock=stock; }
    public Long getId(){return id;} public String getSku(){return sku;} public String getName(){return name;} public int getStock(){return stock;}
    public void reserve(int qty){ if(qty<=0 || stock<qty) throw new IllegalStateException("Insufficient stock"); stock-=qty; }
    public void release(int qty){ stock+=qty; }
}
