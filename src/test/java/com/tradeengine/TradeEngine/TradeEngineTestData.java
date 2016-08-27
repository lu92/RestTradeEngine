package com.tradeengine.TradeEngine;

import com.tradeengine.TradeEngine.dto.CategoryInfo;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import com.tradeengine.common.entities.Price;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.NUMBER;
import static com.tradeengine.TradeEngine.dto.productCriteria.ValueType.TEXT;

public class TradeEngineTestData {
    private TradeEngineTestData() {
    }

    public static final long FAKE_CATEGORY_ID = -1;
    public static final long PROPER_CATEGORY_ID = 1;

    public static final long FAKE_PRODUCT_ID = -1;

    public static final String PHONES_PRODUCT_SCHEME =
            "{"
                    + "\"categoryName\": \"Phones\","
                    + "\"productSchemeElements\": [{"
                    + "\"property\": \"producer\","
                    + "\"valueType\": \"TEXT\","
                    + "\"unit\": \"\""
                    + "},"
                    + "{"
                    + "\"property\": \"screen\","
                    + "\"valueType\": \"NUMBER\","
                    + "\"unit\": \"cal\""
                    + "}]"
                    + "}";

    public static final String LAPTOPS_PRODUCT_SCHEME = "{"
            + "  \"categoryName\" : \"Laptops\","
            + "  \"basicProductSchema\" : {"
            + "    \"producer\" : \"java.lang.String.class\","
            + "    \"disk capacity\" : \"java.lang.Integer.class\""
            + "  }"
            + "}";

    public static final String PHONES_CATEGORY_NAME = "Phones";
    public static final String LAPTOPS_CATEGORY_NAME = "Laptops";

    public static final Category PHONES_CATEGORY = Category.builder().name(PHONES_CATEGORY_NAME).productSchemaJsonFigure(PHONES_PRODUCT_SCHEME).build();
    public static final CategoryInfo PHONES_CATEGORY_INFO = CategoryInfo.builder().name(PHONES_CATEGORY_NAME).productSchemaJsonFigure(PHONES_PRODUCT_SCHEME).build();

    public static final Category LAPTOPS_CATEGORY = Category.builder().name(LAPTOPS_CATEGORY_NAME).build();

    public static final ProductSpecification PRODUCT_DESCRIPTION = ProductSpecification.builder().property("alcohol concentration").value("40").unitOfValue("%").valueType(NUMBER).build();

    public static final Product PRODUCT = Product.builder().commercialName("alcohol").price(Price.builder().amount(30).tax(20).price(50).build())
            .productSpecificationList(Arrays.asList(PRODUCT_DESCRIPTION)).build();

    public static final Product PRODUCT_COMPUTER = Product.builder().commercialName("computer").price(Price.builder().amount(3000).tax(600).price(3600).build())
            .productSpecificationList(Arrays.asList(ProductSpecification.builder().property("Capacity").value("256").unitOfValue("GB").valueType(NUMBER).build())).build();

    public static final List<Product> PRODUCT_LIST = new ArrayList<Product>() {{
        add(PRODUCT);
        add(PRODUCT_COMPUTER);
    }};

    public static Product PRODUCT_SGS7 = Product.builder()
            .commercialName("SAMSUNG GALAXY S7")
            .isAvailable(false)
            .quantity(15)
            .price(Price.builder().amount(2000).tax(500).price(2500).currency("PLN").build())
            .productDescription("SAMSUNG GALAXY S7")
            .productSpecificationList(new ArrayList<ProductSpecification>() {{
                add(ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build());
                add(ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build());
            }})
            .imagePath("C://")
            .build();

    public static Product PRODUCT_SGS6 = Product.builder()
            .commercialName("SAMSUNG GALAXY S6")
            .isAvailable(false)
            .quantity(15)
            .price(Price.builder().amount(1000).tax(250).price(1250).currency("PLN").build())
            .productDescription("SAMSUNG GALAXY S6")
            .productSpecificationList(new ArrayList<ProductSpecification>() {{
                add(ProductSpecification.builder().property("screen").value("5.5").unitOfValue("cal").valueType(NUMBER).build());
                add(ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build());
            }})
            .imagePath("C://")
            .build();

    public static Product PRODUCT_SGS4 = Product.builder()
            .commercialName("SAMSUNG GALAXY S4")
            .isAvailable(false)
            .quantity(15)
            .price(Price.builder().amount(500).tax(125).price(625).currency("PLN").build())
            .productDescription("SAMSUNG GALAXY S4")
            .productSpecificationList(new ArrayList<ProductSpecification>() {{
                add(ProductSpecification.builder().property("screen").value("5.0").unitOfValue("cal").valueType(NUMBER).build());
                add(ProductSpecification.builder().property("producer").value("Samsung").unitOfValue("").valueType(TEXT).build());
            }})
            .imagePath("C://")
            .build();

    public static Product PRODUCT_IPHONE_6S = Product.builder()
            .commercialName("IPHONE 6S")
            .isAvailable(false)
            .quantity(15)
            .price(Price.builder().amount(2200).tax(600).price(2800).currency("PLN").build())
            .productDescription("IPHONE 6S")
            .productSpecificationList(new ArrayList<ProductSpecification>() {{
                add(ProductSpecification.builder().property("screen").value("4.7").unitOfValue("cal").valueType(NUMBER).build());
                add(ProductSpecification.builder().property("producer").value("Apple").unitOfValue("").valueType(TEXT).build());
            }})
            .imagePath("C://")
            .build();
}
