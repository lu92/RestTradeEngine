package com.tradeengine.ShoppingHistory.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingHistory
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "shoppingHistoryId")
    private Long shoppingHistoryId;
    private Long customerId;

    private String sintheticId;

    @OneToMany(mappedBy = "shoppingHistory",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<CompletedOrder> completedOrderList;

    private double totalAmount;
    private double totalTaxes;



    public List<CompletedOrder> getCompletedOrderList()
    {
        if (completedOrderList == null)
        {
            completedOrderList = new ArrayList<>();
            return completedOrderList;
        }
        else
            return completedOrderList;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof ShoppingHistory))
            return false;

        ShoppingHistory that = (ShoppingHistory) o;

        if (customerId != that.customerId)
            return false;
        if (Double.compare(that.totalAmount, totalAmount) != 0)
            return false;
        if (Double.compare(that.totalTaxes, totalTaxes) != 0)
            return false;
        return !(sintheticId != null ? !sintheticId.equals(that.sintheticId) : that.sintheticId != null);

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = (int) (customerId ^ (customerId >>> 32));
        result = 31 * result + (sintheticId != null ? sintheticId.hashCode() : 0);
        temp = Double.doubleToLongBits(totalAmount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalTaxes);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
