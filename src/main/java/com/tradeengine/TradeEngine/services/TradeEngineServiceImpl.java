package com.tradeengine.TradeEngine.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.entities.Category;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngine.mappers.TradeEngineMapper;
import com.tradeengine.TradeEngine.repositories.CategoryRepository;
import com.tradeengine.TradeEngine.repositories.ProductRepository;
import com.tradeengine.TradeEngine.repositories.ProductSpecificationRepository;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.tradeengine.common.Message.Status.FAILURE;
import static com.tradeengine.common.Message.Status.SUCCESS;

@Service
@Repository
public class TradeEngineServiceImpl implements TradeEngineService
{
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    //    @Autowired
    //    private ProductValidator productValidator;

    @Autowired
    private TradeEngineMapper tradeEngineMapper;

    @Override
    public CategoryListDto getCategoryList()
    {
        List<Category> categorylist = categoryRepository.findAll();
        if (categorylist.isEmpty())
        {
            return new CategoryListDto(new Message("category list is empty", SUCCESS), true, new ArrayList<>());
        }
        else
        {
            List<String> collectedCategoriesNames = categorylist.stream().map(category -> category.getName()).collect(Collectors.toList());
            return new CategoryListDto(new Message("category list is filled", SUCCESS), false, collectedCategoriesNames);
        }
    }

    @Override
    public CategoryDto getCategory(long categoryId)
    {
        if (categoryRepository.exists(categoryId))
        {
            Category category = categoryRepository.findOne(categoryId);
            CategoryInfo categoryInfo = tradeEngineMapper.convertCategory(category);
            return new CategoryDto(new Message("Category has been delivered!", SUCCESS), categoryInfo);
        }
        else
        {
            return new CategoryDto(new Message("Selected category doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductSchemeDto getProductSchemeForCategory(long categoryId)
    {
        if (categoryRepository.exists(categoryId))
        {
            ObjectMapper mapper = new ObjectMapper();
            ProductSchemeDto productSchemeDto = null;
            try
            {
                ProductScheme productScheme = mapper.readValue(categoryRepository.getOne(categoryId).getProductSchemaJsonFigure(), ProductScheme.class);
                productSchemeDto = new ProductSchemeDto(new Message("Product scheme has been delivered!", SUCCESS), productScheme);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                productSchemeDto = new ProductSchemeDto(new Message("Cannot proper translate product scheme as ProductScheme object!", FAILURE), null);
            }
            finally
            {
                return productSchemeDto;
            }
        }
        else
        {
            return new ProductSchemeDto(new Message("Category doesn't exist!", FAILURE), null);
        }
    }

    @Override
//    @Transactional(readOnly = false)
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto)
    {

        if (categoryRepository.findByName(createCategoryDto.getCategoryName()).isEmpty())
        {

            List<Category> parentCategoryList = categoryRepository.findByName(createCategoryDto.getParentCategoryName());

            Category category = Category.builder()
                    .name(createCategoryDto.getCategoryName())
                    .productSchemaJsonFigure(createCategoryDto.getProductSchemaJsonFigure())
                    .build();

            if (parentCategoryList.isEmpty())
            {
                Category savedCategory = categoryRepository.save(category);
                CategoryInfo categoryInfo = tradeEngineMapper.convertCategory(savedCategory);
                return new CategoryDto(new Message("Category has been created!", SUCCESS), categoryInfo);
            }
            else if (parentCategoryList.size() == 1)
            {
                Category parentCategory = parentCategoryList.get(0);
                category.setParent(parentCategory);
                Category savedCategory = categoryRepository.save(category);
                parentCategory.getSubCategories().add(savedCategory);
                categoryRepository.save(parentCategory);
                CategoryInfo categoryInfo =
                        tradeEngineMapper.convertCategory(categoryRepository.findByName(savedCategory.getName()).get(0));
                return new CategoryDto(new Message("Category has been created!", SUCCESS), categoryInfo);
            }
            else
            {
                return new CategoryDto(new Message("There is multiple occurrences of parent category!", FAILURE), null);
            }
        }
        else
        {
            return new CategoryDto(new Message("Category already exists!", FAILURE), null);
        }
    }

    @Override
    public ProductDto getProduct(long productId)
    {
        if (productRepository.exists(productId))
        {
            ProductInfo productInfo = tradeEngineMapper.convertProduct(productRepository.findOne(productId));
            return new ProductDto(new Message("Product has been delivered!", SUCCESS), productInfo);
        }
        else
        {
            return new ProductDto(new Message("Product doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductListDto getAllProductsForCategory(String categoryName)
    {
        List<Category> categoryList = categoryRepository.findByName(categoryName);

        switch (categoryList.size())
        {
            case 0:
                return new ProductListDto(new Message("Category doesn't exist!", FAILURE), categoryName, 0, new ArrayList<>());

            case 1:
                List<Product> productList = categoryList.get(0).getProductList();
                List<ProductInfo> productInfoList = tradeEngineMapper.convertProductList(productList);
                return new ProductListDto(new Message("Product list has been delivered!", SUCCESS), categoryName, categoryList.size(), productInfoList);

            default:
                return new ProductListDto(new Message("There are multiple occurrence of requested category!", FAILURE), categoryName, 0, new ArrayList<>());
        }
    }

    @Override
    public ProductDto addProduct(long categoryId, Product product)
    {
        if (categoryRepository.exists(categoryId))
        {
            Category category = categoryRepository.findOne(categoryId);
            //            product validation
            //            Errors errors = new
            //            productValidator.validate(product);

            product.getProductSpecificationList().stream()
                    .forEach(productSpecification -> productSpecificationRepository.save(productSpecification));

            product.setCategory(category);
            Product productDb = productRepository.save(product);
            category.getProductList().add(productDb);
            categoryRepository.save(category);
            ProductInfo productInfo = tradeEngineMapper.convertProduct(productDb);
            return new ProductDto(new Message("Product has been added!", SUCCESS), productInfo);
            //            }
            //            else
            //            {
            //                return new ProductDto(new Message("Product is not valid!", FAILURE), null);
            //            }
        }
        else
        {
            return new ProductDto(new Message("Category doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductDto updateProduct(Product product)
    {
        if (productRepository.exists(product.getProductId()))
        {
            Product productDb = productRepository.getOne(product.getProductId());

            product.getProductSpecificationList().stream()
                    .forEach(productSpecification -> productSpecificationRepository.save(productSpecification));

            productDb = productRepository.save(productDb);
            ProductInfo productInfo = tradeEngineMapper.convertProduct(productDb);
            return new ProductDto(new Message("Product has been updated!", SUCCESS), productInfo);
        }
        else
        {
            return new ProductDto(new Message("Product doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductDto activateProduct(long productId)
    {
        if (productRepository.exists(productId))
        {
            Product productDb = productRepository.getOne(productId);
            productDb.setAvailable(true);
            productDb = productRepository.save(productDb);
            ProductInfo productInfo = tradeEngineMapper.convertProduct(productDb);
            return new ProductDto(new Message("Product has been activated!", SUCCESS), productInfo);
        }
        else
        {
            return new ProductDto(new Message("Product doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductDto deactivateProduct(long productId)
    {
        if (productRepository.exists(productId))
        {
            Product productDb = productRepository.getOne(productId);
            productDb.setAvailable(false);
            productDb = productRepository.save(productDb);
            ProductInfo productInfo = tradeEngineMapper.convertProduct(productDb);
            return new ProductDto(new Message("Product has been deactivated!", SUCCESS), productInfo);
        }
        else
        {
            return new ProductDto(new Message("Product doesn't exist!", FAILURE), null);
        }
    }

    @Override
    public ProductListDto findProducts(ProductCriteria productCriteria)
    {
        return null;
    }
}
