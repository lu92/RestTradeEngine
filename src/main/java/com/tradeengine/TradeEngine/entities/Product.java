package com.tradeengine.TradeEngine.entities;

import com.tradeengine.common.entities.Price;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import org.hibernate.annotations.Proxy;

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
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@Proxy(lazy = false)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "soldProductId")
    private Long productId;
    private String commercialName;
    private String productDescription;
    private boolean isAvailable;
    private int quantity;

    @Embedded
    private Price price;

    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ProductSpecification> productSpecificationList;

    public List<ProductSpecification> getProductSpecificationList() {
        if (productSpecificationList == null) {
            productSpecificationList = new ArrayList<>();
        }
        return productSpecificationList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", commercialName='" + commercialName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", isAvailable=" + isAvailable +
                ", quantity=" + quantity +
                ", price=" + price +
                ", imagePath='" + imagePath + '\'' +
                ", category=" + category.getName() +
                ", productSpecificationList=" + productSpecificationList +
                '}';
    }
}
