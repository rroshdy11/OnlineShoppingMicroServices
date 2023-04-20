package SellingCompany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sellinglog")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class SellingLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String productname;
    private String sellingcompanyname;
    private String customername;
    private String shippingcompanyname;
}
