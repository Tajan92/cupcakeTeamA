package app.entities;
import lombok.Getter;

@Getter
public class Cupcake {
    int id;
    String name;
    BottomCupcake bottomCupcake;
    TopCupcake topCupcake;
    double price;

    public Cupcake(int id, String name, BottomCupcake bottomCupcake, TopCupcake topCupcake) {
        this.id = id;
        this.name = name;
        this.bottomCupcake = bottomCupcake;
        this.topCupcake = topCupcake;
        this.price = bottomCupcake.getPrice()+ topCupcake.getPrice();
    }

}
