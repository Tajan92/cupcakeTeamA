package app.entities;

import lombok.Getter;
import lombok.AllArgsConstructor;
@Getter
@AllArgsConstructor

public class User {
    private int id;
    private String email;
    private String password;
}
