package Admin;

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
public class Admin {
    @Id
    private String username;
    private String password;
    private String firstName;
    private String secondName;

}
