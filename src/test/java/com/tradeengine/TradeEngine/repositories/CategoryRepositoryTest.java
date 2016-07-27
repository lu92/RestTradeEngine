package com.tradeengine.TradeEngine.repositories;

import com.tradeengine.TradeEngine.TradeEngineTestContext;
import com.tradeengine.TradeEngine.entities.Category;
import static org.fest.assertions.Assertions.*;

import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.common.entities.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TradeEngineTestContext.class })
@Rollback(true)
public class CategoryRepositoryTest
{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    public void test()
    {
        assertThat(categoryRepository).isNotNull();

        Category computers = Category.builder().name("Computers").build();

        String phonesProductScheme = "{"
                + "  \"categoryName\" : \"Phones\","
                + "  \"parentCategoryName\" : \"Electronic\","
                + "  \"basicProductSchema\" : {"
                + "    \"producent\" : \"java.lang.String.class\","
                + "    \"screen\" : \"java.lang.Integer.class\""
                + "  }"
                + "}";

        Product SGS7 = Product.builder()
                .commercialName("SAMSUNG GALAXY S7")
                .isAvailable(true)
                .quantity(15)
                .price(Price.builder().amount(2000).tax(500).price(2500).currency("PLN").build())
                .imagePath("C://")
                .build();

        Product savesSGS7 = productRepository.save(SGS7);

        Category phones = Category.builder()
                .name("Phones")
                .productSchemaJsonFigure(phonesProductScheme)
                .productList(Arrays.asList(savesSGS7))
                .build();

//        Category phones = Category.builder().name("PC").parent(computers).build();
        Category laptops = Category.builder().name("Laptops").build();
//        Category laptops = Category.builder().name("Laptops").parent(computers).build();

        categoryRepository.save(phones);
        categoryRepository.save(laptops);

        computers.getSubCategories().addAll(Arrays.asList(phones, laptops));

        Category computersDb = categoryRepository.save(computers);
        assertThat(categoryRepository.count()).isEqualTo(3);

    }
}
