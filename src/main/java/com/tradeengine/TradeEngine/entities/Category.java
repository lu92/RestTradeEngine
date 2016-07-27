package com.tradeengine.TradeEngine.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category
{
    @Id @GeneratedValue @Column(name = "categoryId")
    private Long categoryId;

    @NotNull
    private String name;

    @OneToMany @OrderColumn @JoinColumn(name = "parent_id")
    private List<Category> subCategories;

    @ManyToOne(fetch = FetchType.EAGER) @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private Category parent;

    private String productSchemaJsonFigure;

    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> productList;

    public List<Product> getProductList()
    {
        if (productList == null)
            productList = new ArrayList<>();

        return productList;
    }

    public List<Category> getSubCategories()
    {
        if (subCategories == null)
        {
            subCategories = new ArrayList<>();
        }
        return subCategories;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Category))
            return false;

        Category category = (Category) o;

        return name.equals(category.name);

    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
