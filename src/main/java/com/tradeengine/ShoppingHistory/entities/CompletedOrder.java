package com.tradeengine.ShoppingHistory.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CompletedOrder
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "orderId")
    private Long orderId;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeOfSale;

    @OneToMany(mappedBy = "completedOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<SoldProduct> soldProductsList;

    @ManyToOne
    @JoinColumn(name = "shoppingHistoryId")
    private ShoppingHistory shoppingHistory;

    private String syntheticId;

    private long gainedPoints;
//    private double amount;
//    private double tax;
//    private double price;

    @Embedded
    private Price price;

    public List<SoldProduct> getSoldProductsList()
    {
        if (soldProductsList == null)
        {
            soldProductsList = new ArrayList<>();
            return soldProductsList;
        }
        else
            return soldProductsList;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof CompletedOrder))
            return false;

        CompletedOrder that = (CompletedOrder) o;

        if (gainedPoints != that.gainedPoints)
            return false;
        if (Double.compare(that.price.getAmount(), price.getAmount()) != 0)
            return false;
        if (Double.compare(that.price.getTax(), price.getTax()) != 0)
            return false;
        if (Double.compare(that.price.getPrice(), price.getPrice()) != 0)
            return false;
        if (timeOfSale != null ? !timeOfSale.equals(that.timeOfSale) : that.timeOfSale != null)
            return false;
        return syntheticId.equals(that.syntheticId);

    }

    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = timeOfSale != null ? timeOfSale.hashCode() : 0;
        result = 31 * result + syntheticId.hashCode();
        result = 31 * result + (int) (gainedPoints ^ (gainedPoints >>> 32));
        temp = Double.doubleToLongBits(price.getAmount());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price.getTax());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(price.getPrice());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
