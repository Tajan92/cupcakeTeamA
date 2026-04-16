package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Basket {
    private int basketId;
    //The integer here is amount per cupcakes
    private LinkedHashMap<Cupcake, Integer> cupcakes;


    public double getTotalPrice() {
        double price = 0;
        for (Map.Entry<Cupcake, Integer> cupcake : cupcakes.entrySet()) {
            price += cupcake.getKey().getPrice();
        }
        return price;
    }


}
