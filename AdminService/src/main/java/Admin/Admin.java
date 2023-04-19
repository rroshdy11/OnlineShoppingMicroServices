package Admin;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="admin")
public class Admin implements Serializable {
    @Id
    @Column(name = "username")
    private int username;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "fullName")
    private String fullName;
    @Basic
    @Column(name = "firstName")
    private String firstName;

    public Admin(int username, String password, String fullName, String firstName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.firstName = firstName;
    }

    public Admin() {

    }


    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Admin admin = (Admin) o;

        if (username != admin.username) return false;
        if (password != null ? !password.equals(admin.password) : admin.password != null) return false;
        if (fullName != null ? !fullName.equals(admin.fullName) : admin.fullName != null) return false;
        if (firstName != null ? !firstName.equals(admin.firstName) : admin.firstName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        return result;
    }
}
