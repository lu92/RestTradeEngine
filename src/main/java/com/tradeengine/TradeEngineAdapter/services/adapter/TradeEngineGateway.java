package com.tradeengine.TradeEngineAdapter.services.adapter;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.dto.CustomerInfo;
import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.downlines.BasketRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ProfileReaderRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ShoppingHistoryRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.TradeEngineRestService;
import com.tradeengine.TradeEngineAdapter.services.layers.CustomerSupportLayer;
import com.tradeengine.TradeEngineAdapter.services.layers.TradeEngineSupportLayer;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeEngineGateway implements Adapter, CustomerSupportLayer, TradeEngineSupportLayer {

    private ProfileReaderRestService profileReaderRestService;
    private TradeEngineRestService tradeEngineRestService;
    private ShoppingHistoryRestService shoppingHistoryRestService;
    private BasketRestService basketRestService;
    private CustomerShoppingHistoryInfoBuilder customerShoppingHistoryInfoBuilder;

    @Autowired
    public TradeEngineGateway(ProfileReaderRestService profileReaderRestService,
                              TradeEngineRestService tradeEngineRestService,
                              ShoppingHistoryRestService shoppingHistoryRestService,
                              BasketRestService basketRestService) {
        this.profileReaderRestService = profileReaderRestService;
        this.tradeEngineRestService = tradeEngineRestService;
        this.shoppingHistoryRestService = shoppingHistoryRestService;
        this.basketRestService = basketRestService;
        this.customerShoppingHistoryInfoBuilder = new CustomerShoppingHistoryInfoBuilder(tradeEngineRestService);
    }

    @Override
    public CustomerDto getCustomer(long customerId) {
        return profileReaderRestService.getCustomer(customerId);
    }

    @Override
    public CustomerDtoList getCustomerList() {
        return profileReaderRestService.getCustomerList();
    }

    @Override
    public CustomerDto deleteCustomer(long customerId) {
        return profileReaderRestService.deleteCustomer(customerId);
    }

    @Override
    public CustomerDto createCustomer(CreateCustomerDto createCustomerDto) {
        return profileReaderRestService.createCustomer(createCustomerDto);
    }

    @Override
    public CustomerDto updateCustomer(CustomerInfo customerInfo) {
        return profileReaderRestService.updateCustomer(customerInfo);
    }

    @Override
    public CustomerDto login(String username, String password) {
        return profileReaderRestService.login(username, password);
    }

    @Override
    public CustomerDto createCustomerAndHisShoppingHistory(CreateCustomerDto createCustomerDto) {
        CustomerDto customer = profileReaderRestService.createCustomer(createCustomerDto);
        if (customer.getMessage().getStatus() == Message.Status.SUCCESS) {
            com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto shoppingHistory = shoppingHistoryRestService.createShoppingHistory(customer.getCustomer().getCustomerId());
            if (shoppingHistory.getMessage().getStatus() == Message.Status.SUCCESS) {
                return customer;
            }
            return new CustomerDto(shoppingHistory.getMessage(), null);
        } else {
            return customer;
        }
    }

    @Override
    public CustomerDTO getCustomerWithShoppingHistory(long customerId) {
        CustomerDto customer = profileReaderRestService.getCustomer(customerId);
        if (customer.getMessage().getStatus() == Message.Status.FAILURE) {
            return new CustomerDTO(customer.getMessage(), null);
        } else {
            com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto shoppingHistory = shoppingHistoryRestService.getShoppingHistory(customerId);
            if (shoppingHistory.getMessage().getStatus() == Message.Status.FAILURE) {
                return new CustomerDTO(shoppingHistory.getMessage(), null);
            } else {
//                shoppingHistory.getShoppingHistory().getCompletedOrderList().stream()
//                        .map(completedOrder ->  completedOrder.getSoldProductsList()tradeEngineRestService.getProduct());
//                CustomerDTO fullInfoAboutCustomer = createFullInfoAboutCustomer(customer, shoppingHistory);
                com.tradeengine.TradeEngineAdapter.model.Customer totalInfoCustomer = customerShoppingHistoryInfoBuilder
//                .customer(pr_customer)
//                .shoppingHistory(sh_shopping_history)
                        .params(customer, shoppingHistory)
                        .build();

                CustomerDTO customerDTO = new CustomerDTO(new Message("Customer with his shopping history has been delivered!", Message.Status.SUCCESS), totalInfoCustomer);
                return customerDTO;
//                return fullInfoAboutCustomer;
            }
        }
    }

    @Override
    public Order doShopping(Basket basket) {
        return basketRestService.doShopping(basket);
    }

    @Override
    public CategoryListDto getCategoryList() {
        return tradeEngineRestService.getCategoryList();
    }

    @Override
    public CategoryDto getCategory(long categoryId) {
        return tradeEngineRestService.getCategory(categoryId);
    }

    @Override
    public ProductSchemeDto getProductSchemeForCategory(long categoryId) {
        return tradeEngineRestService.getProductSchemeForCategory(categoryId);
    }

    @Override
    public CategoryDto createCategory(CreateCategoryDto createCategoryDto) {
        return tradeEngineRestService.createCategory(createCategoryDto);
    }

    @Override
    public ProductDto getProduct(long productId) {
        return tradeEngineRestService.getProduct(productId);
    }

    @Override
    public ProductListDto getProductList(RequestedProductsDto requestedProductsDto) {
        return tradeEngineRestService.getProductList(requestedProductsDto);
    }

    @Override
    public ProductListDto getAllProductsForCategory(String categoryName) {
        return tradeEngineRestService.getAllProductsForCategory(categoryName);
    }

    @Override
    public ProductDto addProduct(CreateProductDto createProductDto) {
        return tradeEngineRestService.addProduct(createProductDto);
    }

    @Override
    public ProductDto updateProduct(Product product) {
        return null;
    }

    @Override
    public ProductDto updateProductQuantity(long productId, int quantity) {
        return null;
    }

    @Override
    public ProductDto activateProduct(long productId) {
        return tradeEngineRestService.activateProduct(productId);
    }

    @Override
    public ProductDto deactivateProduct(long productId) {
        return tradeEngineRestService.deactivateProduct(productId);
    }

    @Override
    public ProductListDto findProducts(ProductCriteria productCriteria) {
        return tradeEngineRestService.findProducts(productCriteria);
    }
}
