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

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User(int id, String email){
        this.id = id;
        this.email = email;
    }
}
