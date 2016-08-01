package com.tradeengine.TradeEngine.entities;

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
            .property("Capacity").value("256").unitOfValue("GB").valueType("java.lang.Integer.class").build();

        ProductSpecification author = ProductSpecification.builder()
            .property("Author").value("George R. R. Martin").unitOfValue("").valueType("java.lang.String.class").build();

    */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productDescriptionId;
    private String property;
    private String value;
    private String unitOfValue;
    private String valueType;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
