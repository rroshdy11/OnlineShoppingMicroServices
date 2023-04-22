package com.example.ShoppingApp.SellingCompany;

import jakarta.persistence.*;
import lombok.*;

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

}

