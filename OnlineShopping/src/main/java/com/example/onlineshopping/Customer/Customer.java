package com.example.onlineshopping.Customer;

import com.example.onlineshopping.Notifications.CustomerNotification;
import com.example.onlineshopping.Product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
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
    @OneToMany(mappedBy = "customer" , cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CustomerNotification> customerNotifications;
}
