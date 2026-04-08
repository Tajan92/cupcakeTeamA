package app.services.teamA;
import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    static List<String> message = new ArrayList<>();

    public static List<String> validate(String username, String password, String passwordCheck) {
        validateUser(username, password);
        validatePassword(username, password);
        tooShortUsername(username, password);
        tooShortPassword(username, password);
        passwordMustContainNumber(username, password);
        shouldRejectPasswordWithoutSpecialCharacter(username, password);
        passwordsMustMatch(password, passwordCheck);
        return message;
    }

    public static void passwordsMustMatch(String password, String passwordCheck){
        if (!password.equals(passwordCheck)){
            message.add("Adgangskoder skal være ens");
        }
    }

    public static void validateUser(String username, String password) {
        if (username.isBlank() || username == null) {
            message.add("Brugernavn skal udfyldes");
        }
    }

    public static void validatePassword(String username, String password) {
        if (password.isBlank() || password == null) {
            message.add("Adgangskode skal udfyldes");
        }
    }

    public static void tooShortUsername(String username, String password) {
        if (username.length() < 3) {
            message.add("Brugernavn er for kort");
        }
    }

    public static void tooShortPassword(String username, String password) {
        if (password.length() < 8) {
            message.add("Adgangskode er for kort");
        }
    }

    public static void passwordMustContainNumber(String username, String password) {
        if (!password.chars().anyMatch(Character::isDigit)) {
            message.add("Adgangskode skal indeholde tal");
        }
    }

    public static void shouldRejectPasswordWithoutSpecialCharacter(String username, String password) {
        if (!password.chars().anyMatch(c -> !Character.isLetterOrDigit(c))) {
            message.add("Adgangskode skal indeholde mindst et specialtegn");
        }
    }
}