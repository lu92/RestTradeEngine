package com.tradeengine.TradeEngineAdapter.services.processors;

import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.TradeEngineAdapter.model.Customer;
import com.tradeengine.TradeEngineAdapter.model.Price;
import com.tradeengine.TradeEngineAdapter.model.Product;
import com.tradeengine.TradeEngineAdapter.model.ProductSpecification;

class CustomerShoppingHistoryInfoBuilder
{
    private Customer customer;

    public CustomerShoppingHistoryInfoBuilder()
    {
        customer = new Customer();
    }

    public CustomerShoppingHistoryInfoBuilder customer(CustomerDto pr_customer)
    {
        customer.customerId(pr_customer.getCustomer().getCustomerId())
                .firstname(pr_customer.getCustomer().getFirstname())
                .lastname(pr_customer.getCustomer().getLastname())
                .email(pr_customer.getCustomer().getEmail())
                .birthday(pr_customer.getCustomer().getBirthday())
                .address(pr_customer.getCustomer().getAddress())
                .creditCard(pr_customer.getCustomer().getCreditCard())
                .username(pr_customer.getCustomer().getUsername())
                .creationDate(pr_customer.getCustomer().getCreationDate())
                .points(pr_customer.getCustomer().getPoints())
                .tierLevel(pr_customer.getCustomer().getTierLevel());
        return this;
    }

    public CustomerShoppingHistoryInfoBuilder shoppingHistory(ShoppingHistoryDto sh_shopping_hostory)
    {

//        customer.shoppingHistory(new ShoppingHistory().totalPrice(sh_shopping_hostory.getShoppingHistory().get));
        return this;
    }

    private Product mapProduct(com.tradeengine.TradeEngine.entities.Product product)
    {
        return new Product();
    }

    private ProductSpecification mapProductSpecification(com.tradeengine.TradeEngine.entities.ProductSpecification ps)
    {
        return new ProductSpecification(ps.getProperty(), ps.getValue(), ps.getUnitOfValue(), ps.getValueType());
    }

    private Price mapPrice(com.tradeengine.common.entities.Price price)
    {
        return new Price(price.getAmount(), price.getTax(), price.getPrice(), price.getCurrency());
    }

    public Customer build()
    {
        return customer;
    }
}
