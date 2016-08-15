package com.tradeengine.TradeEngine.entities;

import com.tradeengine.TradeEngine.dto.productCriteria.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.annotations.Proxy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Optional;

@Data
@Entity
@Builder
@Proxy(lazy=false)
@NoArgsConstructor
@AllArgsConstructor
public class ProductSpecification
{
    /*
        ProductSpecificationRepository class holds property with its value
        valueType determines how value will be interpreted
        examples:

        ProductSpecification hardDrive = ProductSpecification.builder()
            .property("Capacity").value("256").unitOfValue("GB").valueType(NUMBER).build();

        ProductSpecification author = ProductSpecification.builder()
            .property("Author").value("George R. R. Martin").unitOfValue("").valueType(TEXT).build();

    */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDescriptionId;
    private String property;
    private String value;
    private String unitOfValue;
    private ValueType valueType;

    @ManyToOne
    @JoinColumn(name = "soldProductId")
    private Product product;

    @Override
    public String toString() {
        return "ProductSpecification{" +
                "productDescriptionId=" + productDescriptionId +
                ", property='" + property + '\'' +
                ", value='" + value + '\'' +
                ", unitOfValue='" + unitOfValue + '\'' +
                ", valueType=" + valueType +
                '}';
    }
}
