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
    private int basketId;

    public User(int id, String email, String password, String role, int basketId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.basketId = basketId;
    }

    public User(int id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int id, String email, Double balance){
        this.id = id;
        this.email = email;
        this.balance = balance;
    }


}
