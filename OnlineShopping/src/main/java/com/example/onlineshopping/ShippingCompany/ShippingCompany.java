package com.example.onlineshopping.ShippingCompany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shippingcompany")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShippingCompany {
    @Id
    private String username;
    private String password;
    private String geography;
    private String email;
    private String phone;

}
