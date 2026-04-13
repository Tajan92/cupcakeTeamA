package app.entities;

import lombok.Getter;
import lombok.AllArgsConstructor;
@Getter
@AllArgsConstructor

public class User {
    private int id;
    private String email;
    private String password;
    private String role;
    private Double balance;

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
