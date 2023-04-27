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
    private String shippingState;
}
