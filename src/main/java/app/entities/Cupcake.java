package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class Cupcake {
    int id;
    String name;
    String top;
    String bottom;
    double price;
    int amount;

    public Cupcake(int id, String name, String top, String bottom, double price) {
        this.id = id;
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.price = price;
    }
}
