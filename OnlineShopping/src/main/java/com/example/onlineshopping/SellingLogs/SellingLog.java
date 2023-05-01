package com.example.onlineshopping.SellingLogs;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SellingLog")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class SellingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productId;
    private String sellingCompanyName;
    private String customerName;
    private String shippingCompanyName;
    private String shippingAddress;
    //when the selling log is created, the shipping state is set to "shipping request"
    //when the shipping company accepts the shipping request, the shipping state is set to "Shipping Company Assigned"
    //when the shipping company delivers the product, the shipping state is set to "Delivered"
    private String shippingState;

}
