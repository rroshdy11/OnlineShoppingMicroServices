package com.example.onlineshopping.SellingCompany;

import com.example.onlineshopping.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sellingcompany")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SellingCompany {
    @Id
    private String name;
    private String password;
    private String email;
    private String phone;
    private float balance;
    @OneToMany(mappedBy = "sellingCompany" , cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Product> products;


}