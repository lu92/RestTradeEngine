package com.tradeengine.ShoppingHistory.mapper;

import com.tradeengine.ShoppingHistory.dto.CompletedOrderInfo;
import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryInfo;
import com.tradeengine.ShoppingHistory.dto.SoldProductInfo;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.entities.SoldProduct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShoppingHistoryMapper {

    public ShoppingHistoryInfo mapShoppingHistory(ShoppingHistory shoppingHistory) {
        ShoppingHistoryInfo shoppingHistoryInfo = new ShoppingHistoryInfo();

        if (shoppingHistory.getShoppingHistoryId() != null)
            shoppingHistoryInfo.setShoppingHistoryId(shoppingHistory.getShoppingHistoryId());

        if (shoppingHistory.getCustomerId() != null)
            shoppingHistoryInfo.setCustomerId(shoppingHistory.getCustomerId());

        List<CompletedOrderInfo> completedOrderInfoList = shoppingHistory.getCompletedOrderList().stream()
                .map(this::mapCompletedOrder)
                .collect(Collectors.toList());

        shoppingHistoryInfo.setCompletedOrderList(completedOrderInfoList);

        if (shoppingHistory.getSpendMoney() != null)
            shoppingHistoryInfo.setSpendMoney(shoppingHistory.getSpendMoney());

        return shoppingHistoryInfo;
    }

    private CompletedOrderInfo mapCompletedOrder(CompletedOrder completedOrder) {
        CompletedOrderInfo completedOrderInfo = new CompletedOrderInfo();

        if (completedOrder.getOrderId() != null)
            completedOrderInfo.setCompletedOrderId(completedOrder.getOrderId());

        if (completedOrder.getTimeOfSale() != null)
            completedOrderInfo.setTimeOfSale(completedOrder.getTimeOfSale());


        List<SoldProductInfo> soldProductInfoList = completedOrder.getSoldProductsList().stream()
                .map(soldProduct -> mapSoldProduct(soldProduct))
                .collect(Collectors.toList());

        completedOrderInfo.setSoldProductsList(soldProductInfoList);

        if (completedOrder.getCost() != null)
            completedOrderInfo.setCost(completedOrder.getCost());

        if (completedOrder.getAddress() != null)
            completedOrderInfo.setAddress(completedOrder.getAddress());


        completedOrderInfo.setGainedPoints(completedOrder.getGainedPoints());

        return completedOrderInfo;
    }

    private SoldProductInfo mapSoldProduct(SoldProduct soldProduct) {
        SoldProductInfo soldProductInfo = new SoldProductInfo();

        if (soldProduct.getSoldProductId() != null)
            soldProductInfo.setSoldProductId(soldProduct.getSoldProductId());

        if (soldProduct.getProductID() != null)
            soldProductInfo.setProductID(soldProduct.getProductID());

        soldProductInfo.setQuantity(soldProduct.getQuantity());

        if (soldProduct.getPrice() != null)
            soldProductInfo.setPrice(soldProduct.getPrice());

//        if (soldProduct.getTotalPrice() != null)
//            soldProductInfo.setTotalPrice(soldProduct.getTotalPrice());

        return soldProductInfo;
    }

    public CompletedOrder mapCompletedOrder(CreateCompletedOrderDto createCompletedOrderDto) {
        CompletedOrder completedOrder = new CompletedOrder();

        if (createCompletedOrderDto.getTimeOfSale() != null)
            completedOrder.setTimeOfSale(createCompletedOrderDto.getTimeOfSale());

        List<SoldProduct> soldProductList = createCompletedOrderDto.getSoldProductsList().stream()
                .map(soldProductInfo -> mapSoldProduct(soldProductInfo))
                .collect(Collectors.toList());

        completedOrder.setSoldProductsList(soldProductList);

        if (createCompletedOrderDto.getAddress() != null)
            completedOrder.setAddress(createCompletedOrderDto.getAddress());

        completedOrder.setGainedPoints(createCompletedOrderDto.getGainedPoints());

        if (createCompletedOrderDto.getCost() != null)
            completedOrder.setCost(createCompletedOrderDto.getCost());


        return completedOrder;
    }

    public SoldProduct mapSoldProduct(SoldProductInfo soldProductInfo) {
        SoldProduct soldProduct = new SoldProduct();

        if (soldProductInfo.getSoldProductId() != null)
            soldProduct.setSoldProductId(soldProductInfo.getSoldProductId());

        if (soldProductInfo.getProductID() != null)
            soldProduct.setProductID(soldProductInfo.getProductID());

        soldProduct.setQuantity(soldProductInfo.getQuantity());

        if (soldProductInfo.getPrice() != null)
            soldProduct.setPrice(soldProductInfo.getPrice());

        return soldProduct;
    }

    public CreateCompletedOrderDto mapCreateCompletedOrder(Long customerId, CompletedOrder completedOrder) {
        CreateCompletedOrderDto createCompletedOrderDto = new CreateCompletedOrderDto();
        createCompletedOrderDto.setCustomerId(customerId);

        if (completedOrder.getTimeOfSale() != null)
            createCompletedOrderDto.setTimeOfSale(completedOrder.getTimeOfSale());

        List<SoldProductInfo> mappedProducts = completedOrder.getSoldProductsList().stream()
                .map(this::mapSoldProduct)
                .collect(Collectors.toList());

        createCompletedOrderDto.setSoldProductsList(mappedProducts);

        if (completedOrder.getAddress() != null)
            createCompletedOrderDto.setAddress(completedOrder.getAddress());

        if (completedOrder.getGainedPoints() != null)
            createCompletedOrderDto.setGainedPoints(completedOrder.getGainedPoints());

        if (completedOrder.getCost() != null)
            createCompletedOrderDto.setCost(completedOrder.getCost());

        return createCompletedOrderDto;
    }
}
