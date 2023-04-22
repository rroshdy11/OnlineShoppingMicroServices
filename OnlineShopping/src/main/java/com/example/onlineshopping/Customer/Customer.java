package com.example.onlineshopping.Customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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
}
