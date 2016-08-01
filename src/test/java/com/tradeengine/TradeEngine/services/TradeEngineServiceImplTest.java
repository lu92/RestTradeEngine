package com.tradeengine.TradeEngine.services;

import com.tradeengine.TradeEngine.TradeEngineServiceContext;
import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CategoryListDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngine.dto.ProductListDto;
import com.tradeengine.TradeEngine.dto.ProductSchemeDto;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.entities.ProductSpecification;
import com.tradeengine.TradeEngine.repositories.CategoryRepository;
import com.tradeengine.TradeEngine.repositories.ProductRepository;
import com.tradeengine.TradeEngine.repositories.ProductSpecificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static com.tradeengine.TradeEngine.TradeEngineTestData.FAKE_CATEGORY_ID;
import static com.tradeengine.TradeEngine.TradeEngineTestData.FAKE_PRODUCT_ID;
import static com.tradeengine.TradeEngine.TradeEngineTestData.LAPTOPS_PRODUCT_SCHEME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_PRODUCT_SCHEME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_COMPUTER;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_SGS6;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PRODUCT_SGS7;
import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { TradeEngineServiceContext.class })
@Rollback(true)
public class TradeEngineServiceImplTest
{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    @Autowired
    private TradeEngineServiceImpl tradeEngineService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Transactional
    public void testCreateAndGetCategory()
    {

        final CreateCategoryDto createCategoryDto = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto = tradeEngineService.createCategory(createCategoryDto);

        assertThat(CREATE_categoryDto.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(CREATE_categoryDto.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto.getCategoryInfo().getSubCategories()).isEmpty();

        CategoryDto GET_categoryDto = tradeEngineService.getCategory(CREATE_categoryDto.getCategoryInfo().getCategoryId());

        assertThat(GET_categoryDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(GET_categoryDto.getMessage().getMessage()).isEqualTo("Category has been delivered!");

        assertThat(GET_categoryDto.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(GET_categoryDto.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(GET_categoryDto.getCategoryInfo().getSubCategories()).isEmpty();
        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(categoryRepository.findByName("Phones").get(0).getSubCategories()).isEmpty();
    }

    @Test
    @Transactional
    public void testGetCategoryWhenDoesNotExistExpectFailure()
    {
        final CategoryDto GET_categoryDto = tradeEngineService.getCategory(FAKE_CATEGORY_ID);

        assertThat(GET_categoryDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(GET_categoryDto.getMessage().getMessage()).isEqualTo("Selected category doesn't exist!");
        assertThat(GET_categoryDto.getCategoryInfo()).isNull();
        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void testGetCategoryListWhenDoesNotExistAnyCategory()
    {
        final CategoryListDto categoryList = tradeEngineService.getCategoryList();

        assertThat(categoryList.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(categoryList.getMessage().getMessage()).isEqualTo("category list is empty");
        assertThat(categoryList.getCategoryList()).hasSize(0);
        assertThat(categoryRepository.count()).isEqualTo(0);
    }

    @Test
    @Transactional
    public void testCreateTwoTimesSameCategoryExpectFailureForSecond()
    {
        final CreateCategoryDto createCategoryDto_1 = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_1 = tradeEngineService.createCategory(createCategoryDto_1);

        assertThat(CREATE_categoryDto_1.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getName()).isEqualTo("Phones");
        System.out.println(createCategoryDto_1);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getSubCategories()).isEmpty();

        final CreateCategoryDto createCategoryDto_2 = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_2 = tradeEngineService.createCategory(createCategoryDto_2);

        assertThat(CREATE_categoryDto_2.getMessage().getMessage()).isEqualTo("Category already exists!");
        assertThat(CREATE_categoryDto_2.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(CREATE_categoryDto_2.getCategoryInfo()).isNull();

        assertThat(categoryRepository.count()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void testCreateTwoDifferentCategoriesAndThenGetCategoryList()
    {
        final CreateCategoryDto createCategoryDto_1 = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_1 = tradeEngineService.createCategory(createCategoryDto_1);

        assertThat(CREATE_categoryDto_1.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getSubCategories()).isEmpty();

        final CreateCategoryDto createCategoryDto_2 = new CreateCategoryDto("Laptops", null, LAPTOPS_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_2 = tradeEngineService.createCategory(createCategoryDto_2);

        assertThat(CREATE_categoryDto_2.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_2.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getName()).isEqualTo("Laptops");
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(LAPTOPS_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getSubCategories()).isEmpty();

        assertThat(categoryRepository.count()).isEqualTo(2);

        final CategoryListDto categoryList = tradeEngineService.getCategoryList();

        assertThat(categoryList.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(categoryList.getMessage().getMessage()).isEqualTo("category list is filled");
        assertThat(categoryList.getCategoryList()).hasSize(2);
    }

    @Test
    @Transactional
    public void testAddSubcategoryToExistingCategoryExpectSuccess()
    {
        final CreateCategoryDto createCategoryDto_1 = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_1 = tradeEngineService.createCategory(createCategoryDto_1);

        assertThat(CREATE_categoryDto_1.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getSubCategories()).isEmpty();

        final CreateCategoryDto createCategoryDto_2 = new CreateCategoryDto("Laptops", "Phones", LAPTOPS_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_2 = tradeEngineService.createCategory(createCategoryDto_2);

        assertThat(CREATE_categoryDto_2.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_2.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getName()).isEqualTo("Laptops");
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(LAPTOPS_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getSubCategories()).isEmpty();

        assertThat(categoryRepository.count()).isEqualTo(2);

        //        List<Category> phones = categoryRepository.findByName("Phones");

        assertThat(categoryRepository.findByName("Phones").get(0)
                .getSubCategories().get(0).getName()).isEqualTo("Laptops");
    }

    @Test
    @Transactional
    public void testAddTwoSubcategoriesToExistingCategoryExpectSuccess()
    {
        final CreateCategoryDto createCategoryDto_1 = new CreateCategoryDto("Electronic", null, "");
        CategoryDto CREATE_categoryDto_1 = tradeEngineService.createCategory(createCategoryDto_1);
        assertThat(CREATE_categoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);

        final CreateCategoryDto createCategoryDto_2 = new CreateCategoryDto("Phones", "Electronic", PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_2 = tradeEngineService.createCategory(createCategoryDto_2);

        assertThat(CREATE_categoryDto_2.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_2.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_2.getCategoryInfo().getSubCategories()).isEmpty();

        final CreateCategoryDto createCategoryDto_3 = new CreateCategoryDto("Laptops", "Electronic", LAPTOPS_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_3 = tradeEngineService.createCategory(createCategoryDto_3);

        assertThat(CREATE_categoryDto_3.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_3.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_3.getCategoryInfo().getName()).isEqualTo("Laptops");
        assertThat(CREATE_categoryDto_3.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(LAPTOPS_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_3.getCategoryInfo().getSubCategories()).isEmpty();

        assertThat(categoryRepository.count()).isEqualTo(3);

        assertThat(categoryRepository.findByName("Electronic").get(0)
                .getSubCategories().get(0).getName()).isEqualTo("Phones");

        assertThat(categoryRepository.findByName("Electronic").get(0)
                .getSubCategories().get(1).getName()).isEqualTo("Laptops");
    }

    @Test
    @Transactional
    public void testGetProductSchemeForCategoryExpectSuccess()
    {
        final CreateCategoryDto createCategoryDto_1 = new CreateCategoryDto("Phones", null, PHONES_PRODUCT_SCHEME);
        CategoryDto CREATE_categoryDto_1 = tradeEngineService.createCategory(createCategoryDto_1);

        assertThat(CREATE_categoryDto_1.getMessage().getMessage()).isEqualTo("Category has been created!");
        assertThat(CREATE_categoryDto_1.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getName()).isEqualTo("Phones");
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getProductSchemaJsonFigure()).isEqualTo(PHONES_PRODUCT_SCHEME);
        assertThat(CREATE_categoryDto_1.getCategoryInfo().getSubCategories()).isEmpty();

        assertThat(categoryRepository.count()).isEqualTo(1);

        ProductSchemeDto GET_productSchemeForCategory = tradeEngineService.getProductSchemeForCategory(CREATE_categoryDto_1.getCategoryInfo().getCategoryId());
        assertThat(GET_productSchemeForCategory.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(GET_productSchemeForCategory.getMessage().getMessage()).isEqualTo("Product scheme has been delivered!");
        assertThat(GET_productSchemeForCategory.getProductScheme().getCategoryName()).isEqualTo("Phones");
        assertThat(GET_productSchemeForCategory.getProductScheme().getBasicProductSchema().get("producer")).isEqualTo("java.lang.String.class");
        assertThat(GET_productSchemeForCategory.getProductScheme().getBasicProductSchema().get("screen")).isEqualTo("java.lang.Integer.class");
    }

    @Test
    @Transactional
    public void testGetProductSchemeWhenCategoryDoesNotExistExpectFailure()
    {
        final ProductSchemeDto productSchemeForCategory = tradeEngineService.getProductSchemeForCategory(FAKE_CATEGORY_ID);

        assertThat(productSchemeForCategory.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(productSchemeForCategory.getMessage().getMessage()).isEqualTo("Category doesn't exist!");
        assertThat(productSchemeForCategory.getProductScheme()).isNull();
    }

    //      PRODUCT's TESTS

    @Test
    @Transactional
    public void testGetProductWhenDoesNotExistExpectFailure()
    {
        ProductDto productDto = tradeEngineService.getProduct(FAKE_PRODUCT_ID);

        assertThat(productDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(productDto.getMessage().getMessage()).isEqualTo("Product doesn't exist!");
        assertThat(productDto.getProduct()).isNull();
    }

    @Test
    @Transactional
    public void testGetProductWhenExistExpectSuccess()
    {
        Product product = productRepository.save(PRODUCT_COMPUTER);

        ProductDto productDto = tradeEngineService.getProduct(product.getProductId());

        assertThat(productDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(productDto.getMessage().getMessage()).isEqualTo("Product has been delivered!");
        assertThat(productDto.getProduct()).isNotNull();
    }

    @Test
    @Transactional
    public void addProductAndActivateExpectSuccess()
    {
        final CreateCategoryDto createCategoryDto = new CreateCategoryDto("Phones", "Electronic", PHONES_PRODUCT_SCHEME);
        CategoryDto savedCategory = tradeEngineService.createCategory(createCategoryDto);
        assertThat(savedCategory.getMessage().getStatus()).isEqualTo(SUCCESS);

        ProductDto productDto = tradeEngineService.addProduct(savedCategory.getCategoryInfo().getCategoryId(), PRODUCT_SGS7);
        assertThat(productDto.getMessage().getStatus()).isEqualTo(SUCCESS);

        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(productRepository.count()).isEqualTo(1);
        assertThat(productSpecificationRepository.count()).isEqualTo(2);

        Category category = categoryRepository.findByName("Phones").get(0);

        assertThat(category.getSubCategories()).isEmpty();
        assertThat(category.getProductList().size()).isEqualTo(1);
        assertThat(category.getProductList().get(0).getProductSpecificationList().get(0).getProperty()).isEqualTo("screen");
        assertThat(category.getProductList().get(0).getProductSpecificationList().get(1).getProperty()).isEqualTo("producer");

        ProductListDto phoneList = tradeEngineService.getAllProductsForCategory("Phones");
        assertThat(phoneList.getProductList().size()).isEqualTo(1);

        ProductDto activatedProductDto = tradeEngineService.activateProduct(productDto.getProduct().getProductId());
        assertThat(activatedProductDto.getMessage().getStatus()).isEqualTo(SUCCESS);
        assertThat(activatedProductDto.getMessage().getMessage()).isEqualTo("Product has been activated!");
        assertThat(activatedProductDto.getProduct().isAvailable()).isTrue();
    }

    @Test
    @Transactional
    public void activateProductWhichDoesNotExistExpectFailure()
    {
        ProductDto activatedProductDto = tradeEngineService.activateProduct(FAKE_PRODUCT_ID);

        assertThat(activatedProductDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(activatedProductDto.getMessage().getMessage()).isEqualTo("Product doesn't exist!");
        assertThat(activatedProductDto.getProduct()).isNull();
    }

    @Test
    @Transactional
    public void deactivateProductWhichDoesNotExistExpectFailure()
    {
        ProductDto activatedProductDto = tradeEngineService.deactivateProduct(FAKE_PRODUCT_ID);

        assertThat(activatedProductDto.getMessage().getStatus()).isEqualTo(FAILURE);
        assertThat(activatedProductDto.getMessage().getMessage()).isEqualTo("Product doesn't exist!");
        assertThat(activatedProductDto.getProduct()).isNull();
    }

    @Test
    @Transactional
    public void createAndUpdateProductExpectSuccess()
    {
        final CreateCategoryDto createCategoryDto = new CreateCategoryDto("Phones", "Electronic", PHONES_PRODUCT_SCHEME);
        CategoryDto savedCategory = tradeEngineService.createCategory(createCategoryDto);
        assertThat(savedCategory.getMessage().getStatus()).isEqualTo(SUCCESS);

        ProductDto productDto = tradeEngineService.addProduct(savedCategory.getCategoryInfo().getCategoryId(), PRODUCT_SGS6);
        assertThat(productDto.getMessage().getStatus()).isEqualTo(SUCCESS);

        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(productRepository.count()).isEqualTo(1);
        assertThat(productSpecificationRepository.count()).isEqualTo(2);

        //  updating product
        Product updatedProduct = productDto.getProduct();
        updatedProduct.setCommercialName("SAMSUNG GALAXY S5");
        updatedProduct.setProductDescription("SAMSUNG GALAXY S5 Description");
        updatedProduct.getPrice().setAmount(700);
        updatedProduct.getPrice().setTax(150);
        updatedProduct.getPrice().setPrice(850);
        updatedProduct.getProductSpecificationList().add(
                ProductSpecification.builder().property("Memory").value("32").unitOfValue("GB").valueType("java.lang.Integer.class").build()
        );

        assertThat(categoryRepository.count()).isEqualTo(1);
        assertThat(productRepository.count()).isEqualTo(1);
        assertThat(productSpecificationRepository.count()).isEqualTo(3);

        ProductDto updatedProductDto = tradeEngineService.updateProduct(updatedProduct);
        assertThat(updatedProductDto.getProduct().getCommercialName()).isEqualTo("SAMSUNG GALAXY S5");
        assertThat(updatedProductDto.getProduct().getProductDescription()).isEqualTo("SAMSUNG GALAXY S5 Description");
        assertThat(updatedProductDto.getProduct().getPrice().getAmount()).isEqualTo(700);
        assertThat(updatedProductDto.getProduct().getPrice().getTax()).isEqualTo(150);
        assertThat(updatedProductDto.getProduct().getPrice().getPrice()).isEqualTo(850);

        ProductSpecification justAddedProductSpecification = updatedProductDto.getProduct().getProductSpecificationList().get(2);
        assertThat(justAddedProductSpecification.getProperty()).isEqualTo("Memory");
        assertThat(justAddedProductSpecification.getValue()).isEqualTo("32");
        assertThat(justAddedProductSpecification.getUnitOfValue()).isEqualTo("GB");
        assertThat(justAddedProductSpecification.getValueType()).isEqualTo("java.lang.Integer.class");
    }
}
