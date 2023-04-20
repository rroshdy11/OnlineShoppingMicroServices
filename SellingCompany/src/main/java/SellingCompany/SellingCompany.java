package SellingCompany;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sellingcompany")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SellingCompany {
    @Id
    private String name;
    private String password;

}

