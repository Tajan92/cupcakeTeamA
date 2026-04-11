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
}
