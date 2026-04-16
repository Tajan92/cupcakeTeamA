package app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class Cupcake {
    int cupcakeId;
    String name;
    String top;
    String bottom;
    double price;
    int amount;

    public Cupcake(int id, String name, String top, String bottom, double price) {
        this.cupcakeId = id;
        this.name = name;
        this.top = top;
        this.bottom = bottom;
        this.price = price;
    }

    public double getTotalPrice(){
        return this.price * this.amount;
    }
}
