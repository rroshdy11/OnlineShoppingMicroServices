package com.example.onlineshopping.Product;

import com.example.onlineshopping.SellingCompany.SellingCompany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int productId;
    private String productName;
    private String productDescription;
    private int productStock;
    private float productPrice;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellingcompanyname")
    private SellingCompany sellingCompany;
}