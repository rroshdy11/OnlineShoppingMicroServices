package com.example.adminservices.Admin;

import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Stateless
public class Admin {
    @Id
    private String username;
    private String password;
    private String firstName;
    private String secondName;
    private String email;
    private String phone;
    private String address;

}
