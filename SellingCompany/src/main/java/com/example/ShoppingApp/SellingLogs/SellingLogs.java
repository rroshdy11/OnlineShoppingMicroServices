package com.example.ShoppingApp.SellingLogs;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sellinglog")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SellingLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productname;
    private String sellingcompanyname;
    private String customername;
    private String shippingcompanyname;
}
