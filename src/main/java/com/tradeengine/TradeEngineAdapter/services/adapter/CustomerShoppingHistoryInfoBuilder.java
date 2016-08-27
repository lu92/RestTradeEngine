package com.tradeengine.TradeEngineAdapter.services.adapter;

import com.tradeengine.ProfileReader.CustomerDto;
import com.tradeengine.ShoppingHistory.dto.CompletedOrderInfo;
import com.tradeengine.ShoppingHistory.dto.SoldProductInfo;
import com.tradeengine.TradeEngine.dto.ProductDto;
import com.tradeengine.TradeEngineAdapter.model.Customer;
import com.tradeengine.TradeEngineAdapter.model.Order;
import com.tradeengine.TradeEngineAdapter.model.ShoppingHistory;
import com.tradeengine.TradeEngineAdapter.services.downlines.TradeEngineRestService;
import com.tradeengine.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CustomerShoppingHistoryInfoBuilder {

    @Autowired
    private TradeEngineRestService tradeEngineRestService;

    private Customer customer;

    public CustomerShoppingHistoryInfoBuilder() {
        customer = new Customer();
    }

    public CustomerShoppingHistoryInfoBuilder params(CustomerDto pr_customer, com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto sh_shoppingHistoryDto) {
        customer(pr_customer);
        shoppingHistory(sh_shoppingHistoryDto);
        return this;
    }

    private void customer(CustomerDto pr_customer) {
        customer.setCustomerId(pr_customer.getCustomer().getCustomerId());
        customer.setFirstname(pr_customer.getCustomer().getFirstname());
        customer.setLastname(pr_customer.getCustomer().getLastname());
        customer.setEmail(pr_customer.getCustomer().getEmail());
        customer.setBirthday(pr_customer.getCustomer().getBirthday());
        customer.setAddress(pr_customer.getCustomer().getAddress());
        customer.setCreditCard(pr_customer.getCustomer().getCreditCard());
        customer.setUsername(pr_customer.getCustomer().getUsername());
        customer.setCreationDate(pr_customer.getCustomer().getCreationDate());
        customer.setPoints(pr_customer.getCustomer().getPoints());
        customer.setTierLevel(pr_customer.getCustomer().getTierLevel());
    }

    private void shoppingHistory(com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto shoppingHistoryDto) {
        ShoppingHistory shoppingHistory = new ShoppingHistory();
        for (CompletedOrderInfo completedOrder : shoppingHistoryDto.getShoppingHistory().getCompletedOrderList()) {
            shoppingHistory.getOrderList().add(mapOrder(completedOrder));
        }


        shoppingHistory.setTotalPrice(shoppingHistoryDto.getShoppingHistory().getSpendMoney());
        customer.setShoppingHistory(shoppingHistory);
    }

    private Order mapOrder(CompletedOrderInfo completedOrder) {
        Order order = new Order();
//        order.setCustomerId(completedOrder.get);
        order.setTimeOfSale(completedOrder.getTimeOfSale());

        for (SoldProductInfo soldProduct : completedOrder.getSoldProductsList()) {
            ProductDto productDto = tradeEngineRestService.getProduct(soldProduct.getProductID());
            if (productDto.getMessage().getStatus() == Message.Status.SUCCESS) {
                order.getProductList().add(productDto.getProductInfo());
            }
        }
        order.setAddress(completedOrder.getAddress());
        order.setPrice(completedOrder.getCost());
        return order;
    }

    public Customer build() {
        return customer;
    }
}
