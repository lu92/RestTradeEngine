package com.tradeengine.common.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    private double amount;
    private double tax;
    private double price;
    private String currency;

    public void addAmount(double amount) {
        this.amount += amount;
        this.price += amount;
    }

    public void addTax(double tax) {
        this.tax += tax;
        this.price += tax;
    }


    @Override
    public String toString() {
        return "Price{" +
                "amount=" + amount +
                ", tax=" + tax +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                '}';
    }
}
