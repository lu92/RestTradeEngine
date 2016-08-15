package com.tradeengine.TradeEngine.services.ProductSelector;

import com.tradeengine.TradeEngine.dto.productCriteria.Criteria;
import com.tradeengine.TradeEngine.dto.productCriteria.Direct;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import com.tradeengine.TradeEngine.repositories.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class ProductSelector {

    @Autowired
    private CategoryRepository categoryRepository;

    //    return only active products
    public List<Product> find(ProductCriteria productCriteria) throws IllegalArgumentException {

        List<Product> products = getProductsFromCategory(productCriteria.getCategory());
        List<Product> matchedProducts = products.stream()
                .filter(Product::isAvailable)
                .filter(product -> isProductMatched(product, productCriteria))
                .collect(toList());
        return matchedProducts;
    }

    private List<Product> getProductsFromCategory(String category) throws IllegalArgumentException {
        List<Category> categoryList = categoryRepository.findByName(category);

        if (categoryList.size() == 0) {
            throw new IllegalArgumentException("Category does not exist!");
        } else if (categoryList.size() == 1) {
            if (categoryList.get(0).getProductList().isEmpty()) {
                throw new IllegalArgumentException("There is no exist any product");
            } else {
                return categoryList.get(0).getProductList();
            }
        } else {
            throw new IllegalArgumentException("INTERNAL ERROR");
        }
    }

    private boolean isProductMatched(Product product, ProductCriteria productCriteria) {
        if (!StringUtils.equals(product.getCategory().getName(), productCriteria.getCategory())) {
            return false;
        }

        if (productCriteria.getMinPrice() != null && product.getPrice().getPrice() < productCriteria.getMinPrice()) {
            return false;
        }

        if (productCriteria.getMaxPrice() != null && product.getPrice().getPrice() > productCriteria.getMaxPrice()) {
            return false;
        }

        List<String> availableProperties = product.getProductSpecificationList().stream().map(ProductSpecification::getProperty).collect(toList());
        List<String> demandedProperties = productCriteria.getCriteriaList().stream().map(Criteria::getProperty).collect(toList());

        if (demandedProperties.isEmpty())
            return true;

        if (!availableProperties.containsAll(demandedProperties))
            return false;

        else return productCriteria.getCriteriaList().stream().
                allMatch(criteria -> isCriteriaMatched(criteria, product.getProductSpecificationList()));

    }

    public boolean isCriteriaMatched(Criteria criteria, List<ProductSpecification> productSpecificationList) {
        return productSpecificationList.stream().anyMatch(productSpecification -> isCriteriaApplied(criteria, productSpecification));
    }

    public boolean isCriteriaApplied(Criteria criteria, ProductSpecification productSpecification) {
        if (StringUtils.equals(criteria.getProperty(), productSpecification.getProperty()) &&
                StringUtils.equals(criteria.getUnitOfValue(), productSpecification.getUnitOfValue()) &&
                criteria.getValueType() == productSpecification.getValueType()) {

            switch (criteria.getValueType()) {
                case NUMBER:
                    return NumberValue(criteria.getValue(), productSpecification.getValue(), criteria.getDirect());

                case TEXT:
                    return TextValue(criteria.getValue(), productSpecification.getValue(), criteria.getDirect());
            }
        }
        return false;
    }

    private boolean NumberValue(String criteriaValue, String productSpecificationValue, Direct direct) {
        switch (direct) {
            case GREATER_THAN_OR_EQUAL:
                return Double.valueOf(productSpecificationValue) >= Double.valueOf(criteriaValue);

            case LOWER_THAN_OR_EQUAL:
                return Double.valueOf(productSpecificationValue) <= Double.valueOf(criteriaValue);

            case GREATER:
                return Double.valueOf(productSpecificationValue) > Double.valueOf(criteriaValue);

            case LOWER:
                return Double.valueOf(productSpecificationValue) < Double.valueOf(criteriaValue);

            case EQUAL:
                return Double.valueOf(productSpecificationValue).equals(Double.valueOf(criteriaValue));

            default:
                return false;
        }
    }

    private boolean TextValue(String criteriaValue, String productSpecificationValue, Direct direct) {
        switch (direct) {
            case EQUAL:
                return productSpecificationValue.equals(criteriaValue);

            default:
                return false;
        }
    }
}
