package com.tradeengine.ShoppingHistory.entities;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@Proxy(lazy = false)
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shoppingHistoryId")
    private Long shoppingHistoryId;
    private Long customerId;
    private String syntheticId;

    @OneToMany(mappedBy = "shoppingHistory", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CompletedOrder> completedOrderList;

    @Embedded
    private Price spendMoney;


    public List<CompletedOrder> getCompletedOrderList() {
        if (completedOrderList == null) {
            completedOrderList = new ArrayList<>();
        }
        return completedOrderList;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o)
//            return true;
//        if (!(o instanceof ShoppingHistory))
//            return false;
//
//        ShoppingHistory that = (ShoppingHistory) o;
//
//        if (customerId != that.customerId)
//            return false;
//        if (Double.compare(that.totalAmount, totalAmount) != 0)
//            return false;
//        if (Double.compare(that.totalTaxes, totalTaxes) != 0)
//            return false;
//        return !(syntheticId != null ? !syntheticId.equals(that.syntheticId) : that.syntheticId != null);
//
//    }
//
//    @Override
//    public int hashCode() {
//        int result;
//        long temp;
//        result = (int) (customerId ^ (customerId >>> 32));
//        result = 31 * result + (syntheticId != null ? syntheticId.hashCode() : 0);
//        temp = Double.doubleToLongBits(totalAmount);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        temp = Double.doubleToLongBits(totalTaxes);
//        result = 31 * result + (int) (temp ^ (temp >>> 32));
//        return result;
//    }
}
