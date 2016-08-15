package com.tradeengine.TradeEngine.services.ProductSelector;

import com.tradeengine.TradeEngine.dto.productCriteria.Criteria;

import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_SGS7;
import static com.tradeengine.TradeEngine.dto.productCriteria.Direct.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.*;

import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.repositories.CategoryRepository;
import com.tradeengine.TradeEngine.repositories.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import java.util.List;

import static com.tradeengine.TradeEngine.TradeEngineTestData.*;
import static org.fest.assertions.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ProductSelectorTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductSelector productSelector;

    @Test
    public void searchByCategoryNameAndPrice() {

        // given
        List<Product> productList = asList(PRODUCT_SGS4, PRODUCT_SGS6, PRODUCT_SGS7, PRODUCT_IPHONE_6S);
        for (Product product : productList) {
            product.setAvailable(true);
            product.setCategory(PHONES_CATEGORY);
        }

        PHONES_CATEGORY.setProductList(productList);

        Mockito.when(categoryRepository.findByName(PHONES_CATEGORY_NAME)).thenReturn(asList(PHONES_CATEGORY));

        ProductCriteria productCriteria = ProductCriteria.builder()
                .category(PHONES_CATEGORY_NAME)
                .minPrice(2500L)
                .maxPrice(3000L)
                .build();

        // when
        List<Product> matchedProducts = productSelector.find(productCriteria);

        // then
        assertThat(matchedProducts).hasSize(2).containsExactly(PRODUCT_SGS7, PRODUCT_IPHONE_6S);
    }


    @Test
    public void searchByScreenSizeAndProducerAndPrice() {

        // given
        List<Product> productList = asList(PRODUCT_SGS4, PRODUCT_SGS6, PRODUCT_SGS7, PRODUCT_IPHONE_6S);
        for (Product product : productList) {
            product.setAvailable(true);
            product.setCategory(PHONES_CATEGORY);
        }

        PHONES_CATEGORY.setProductList(productList);

        Mockito.when(categoryRepository.findByName(PHONES_CATEGORY_NAME)).thenReturn(asList(PHONES_CATEGORY));

        ProductCriteria productCriteria = ProductCriteria.builder()
                .category(PHONES_CATEGORY_NAME)
                .minPrice(1000L)
                .maxPrice(3000L)
                .criteriaList(asList(
                        new Criteria("screen", "5", "cal", NUMBER, GREATER_THAN_OR_EQUAL),
                        new Criteria("screen", "6", "cal", NUMBER, LOWER),
                        new Criteria("producer", "Samsung", "", TEXT,EQUAL)))
                .build();

        // when
        List<Product> matchedProducts = productSelector.find(productCriteria);

        // then
        assertThat(matchedProducts).hasSize(2).containsOnly(PRODUCT_SGS7, PRODUCT_SGS6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void searchWhenCategoryDoesNotContainAnyProduct() {

        // given
        Mockito.when(categoryRepository.findByName(LAPTOPS_CATEGORY_NAME)).thenReturn(asList(LAPTOPS_CATEGORY));

        ProductCriteria productCriteria = ProductCriteria.builder()
                .category(LAPTOPS_CATEGORY_NAME)
                .minPrice(1000L)
                .maxPrice(3000L)
                .criteriaList(asList(
                        new Criteria("screen", "13", "cal", NUMBER, GREATER_THAN_OR_EQUAL),
                        new Criteria("screen", "17", "cal", NUMBER, LOWER),
                        new Criteria("producer", "Samsung", "", TEXT,EQUAL)))
                .build();

        // when
        List<Product> matchedProducts = productSelector.find(productCriteria);

        // then
        assertThat(matchedProducts).isEmpty();
    }
}
