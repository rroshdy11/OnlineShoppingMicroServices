package com.example.shippingservice.ShippingCompany;

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
public class shippingCompany {
    @Id
    private String name;

    private String password;
    private String geography;

}
