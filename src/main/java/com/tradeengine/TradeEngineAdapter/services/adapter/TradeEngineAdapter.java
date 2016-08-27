package com.tradeengine.TradeEngineAdapter.services.adapter;

import com.tradeengine.ProfileReader.CreateCustomerDto;
import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ProfileReader.CustomerDtoList;
import com.tradeengine.ProfileReader.entities.Customer;
import com.tradeengine.TradeEngine.dto.*;
import com.tradeengine.TradeEngine.dto.productCriteria.ProductCriteria;
import com.tradeengine.TradeEngine.entities.Product;
import com.tradeengine.TradeEngineAdapter.model.Basket;
import com.tradeengine.TradeEngineAdapter.model.Discount;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.model.dto.CustomerDTO;
import com.tradeengine.TradeEngineAdapter.services.downlines.BasketRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ProfileReaderRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ShoppingHistoryRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.TradeEngineRestService;
import com.tradeengine.TradeEngineAdapter.services.layers.BasketSupportLayer;
import com.tradeengine.TradeEngineAdapter.services.layers.CustomerSupportLayer;
import com.tradeengine.TradeEngineAdapter.services.layers.TradeEngineSupportLayer;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TradeEngineAdapter implements Adapter, BasketSupportLayer, CustomerSupportLayer, TradeEngineSupportLayer {

    @Autowired
    private BasketRestService basketRestService;

    @Autowired
    private ProfileReaderRestService profileReaderRestService;

    @Autowired
    private TradeEngineRestService tradeEngineRestService;

    @Autowired
    private ShoppingHistoryRestService shoppingHistoryRestService;

    @Autowired
    private CustomerShoppingHistoryInfoBuilder customerShoppingHistoryInfoBuilder;

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
    public CustomerDto updateCustomer(com.tradeengine.ProfileReader.entities.Customer customer) {
        return profileReaderRestService.updateCustomer(customer);
    }

    @Override
    public CustomerDto login(String username, String password) {
        return null;
    }

//    @Override
//    public CustomerDTO getCustomerWithShoppingHistory(long customerId)
//    {
//        return customerRestService.getCustomerWithShoppingHistory(customerId);
//    }

    public void processOrder(Basket basket) {
        CustomerDto customerDto = profileReaderRestService.getCustomer(basket.getCustomerId());
        if (customerDto.getMessage().getStatus() == Message.Status.SUCCESS) {
            Order order = basketRestService.calculateOrder(basket);
            Customer customer = customerDto.getCustomer();

        }
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
    public void addShoppingHistory(long customerId, Object o) {

    }

    @Override
    public boolean checkProductsAvailability(Basket basket) {
        return false;
    }

    @Override
    public Order calculateOrder(Basket basket) {
        return basketRestService.calculateOrder(basket);
    }

    @Override
    public Order calculatePoints(Order order) {
        return null;
    }

    @Override
    public Order calculateDiscount(Order order) {
        return null;
    }

    @Override
    public Order updateProductsAvailability(Order order) {
        return null;
    }

    @Override
    public void processOrder(Order order) {

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

//    @Override
//    public ProductListDto getProductList(List<Long> productIdList) {
//        return null;
//    }

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

//    private CustomerDTO createFullInfoAboutCustomer(CustomerDto pr_customer, ShoppingHistoryDto sh_shopping_history) {
//        // sprawdzic czy customer ma historie
//
//        CustomerShoppingHistoryInfoBuilder customerShoppingHistoryInfoBuilder =
//                new CustomerShoppingHistoryInfoBuilder();
//
//        com.tradeengine.TradeEngineAdapter.model.Customer customer = customerShoppingHistoryInfoBuilder
////                .customer(pr_customer)
////                .shoppingHistory(sh_shopping_history)
//                .params(pr_customer, sh_shopping_history)
//                .build();
//
//        CustomerDTO customerDTO = new CustomerDTO(new Message("Customer with his shopping history has been delivered!", Message.Status.SUCCESS), customer);
//        return customerDTO;
//    }
}
