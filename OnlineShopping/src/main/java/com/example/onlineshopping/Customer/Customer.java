package com.example.onlineshopping.Customer;

import com.example.onlineshopping.Product.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.ejb.Stateful;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Stateful
public class Customer {
    @Id
    private String username;
    private String password;
    private String address;
    private String email;
    private String phone;
    private String firstName;
    private String secondName;
    private float balance;
    @Transient
    private List<Product> cart;
}
