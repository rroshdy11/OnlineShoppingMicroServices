package com.example.onlineshopping.Customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "customerNotification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotification {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Customer customer;
    private String message;

}
