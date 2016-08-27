package com.tradeengine.ShoppingHistory.entities;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.annotations.Proxy;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Builder
@Entity
@Proxy(lazy = false)
@NoArgsConstructor
@AllArgsConstructor
public class SoldProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long soldProductId;
    private Long productID; //  to determine proper product
    private String sintheticId;
    private int quantity;

    @Embedded
    private Price price;

//    @Embedded
//    private Price totalPrice;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private CompletedOrder completedOrder;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SoldProduct))
            return false;

        SoldProduct that = (SoldProduct) o;

        if (quantity != that.quantity)
            return false;
        if (sintheticId != null ? !sintheticId.equals(that.sintheticId) : that.sintheticId != null)
            return false;
        if (!price.equals(that.price))
            return false;
        return !(completedOrder != null ? !completedOrder.equals(that.completedOrder) : that.completedOrder != null);
    }

    @Override
    public int hashCode() {
        int result = quantity;
        result = 31 * result + price.hashCode();
        result = 31 * result + (completedOrder != null ? completedOrder.hashCode() : 0);
        return result;
    }
}
