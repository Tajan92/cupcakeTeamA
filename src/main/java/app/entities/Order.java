package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@AllArgsConstructor

public class Order {
    private int orderId;
    private int userId;
    private ArrayList<Integer> cupcakeIds;
    private ArrayList<Cupcake> cupcakeList;

    public Order(int orderId, int userId, ArrayList<Integer> cupcakeIds) {
        this.orderId = orderId;
        this.userId = userId;
        this.cupcakeIds = cupcakeIds;
    }

    public Order(ArrayList<Cupcake> cupcakeList, int orderId) {
        this.cupcakeList = cupcakeList;
        this.orderId = orderId;
    }
}
