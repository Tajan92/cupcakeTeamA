package app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class Basket {
    private String amountAndName;
    private double price;
    private String topAndBottom;
}
