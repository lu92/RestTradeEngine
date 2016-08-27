package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryInfo;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.entities.SoldProduct;
import com.tradeengine.ShoppingHistory.mapper.ShoppingHistoryMapper;
import com.tradeengine.ShoppingHistory.repositories.CompletedOrderRepository;
import com.tradeengine.ShoppingHistory.repositories.ShoppingHistoryRepository;
import com.tradeengine.ShoppingHistory.repositories.SoldProductRepository;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingHistoryServiceImpl implements ShoppingHistoryService {

    @Autowired
    private ShoppingHistoryRepository shoppingHistoryRepository;

    @Autowired
    private CompletedOrderRepository completedOrderRepository;

    @Autowired
    private SoldProductRepository soldProductRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShoppingHistoryMapper shoppingHistoryMapper;

    @Override
    public ShoppingHistoryDto getShoppingHistory(long customerId) {
        List<ShoppingHistory> shoppingHistoryList = shoppingHistoryRepository.findByCustomerId(customerId);
        switch (shoppingHistoryList.size()) {
            case 0:
                return new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

            case 1:
                ShoppingHistoryInfo shoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(shoppingHistoryList.get(0));
                return new ShoppingHistoryDto(new Message("Shopping history has been delivered!", Message.Status.SUCCESS), shoppingHistoryInfo);

            default:
                return new ShoppingHistoryDto(new Message("There is more than one shopping history for customer!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public ShoppingHistoryDto createShoppingHistory(long customerId) {
        if (shoppingHistoryRepository.findByCustomerId(customerId).isEmpty()) {
            ShoppingHistory shoppingHistory = ShoppingHistory.builder().customerId(customerId).syntheticId(UUID.randomUUID().toString())
                    .spendMoney(Price.builder().amount(0).tax(0).build()).build();
            ShoppingHistory shoppingHistoryDb = shoppingHistoryRepository.save(shoppingHistory);
            ShoppingHistoryInfo shoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(shoppingHistoryDb);
            return new ShoppingHistoryDto(new Message("Shopping history has been created!", Message.Status.SUCCESS), shoppingHistoryInfo);
        } else {
            return new ShoppingHistoryDto(new Message("Shopping history already exists!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public ShoppingHistoryDto addOrder(CreateCompletedOrderDto createCompletedOrderDto) {
        if (!customerRepository.exists(createCompletedOrderDto.getCustomerId())) {
            return new ShoppingHistoryDto(new Message("Customer doesn't exist", Message.Status.FAILURE), null);
        } else {
            List<ShoppingHistory> shoppingHistoryList = shoppingHistoryRepository.findByCustomerId(createCompletedOrderDto.getCustomerId());
            switch (shoppingHistoryList.size()) {
                case 0:
                    return new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

                case 1:
                    if (createCompletedOrderDto.getSoldProductsList().isEmpty()) {
                        return new ShoppingHistoryDto(new Message("Delivered order doesn't contain any sold products!", Message.Status.FAILURE), null);
                    } else {
                        ShoppingHistory shoppingHistory = shoppingHistoryList.get(0);
                        CompletedOrder completedOrder = shoppingHistoryMapper.mapCompletedOrder(createCompletedOrderDto);
                        completedOrder.setSyntheticId(UUID.randomUUID().toString());
                        // Order Validation

                        shoppingHistory.setSpendMoney(Price.builder()
                                .amount(shoppingHistory.getSpendMoney().getAmount() + createCompletedOrderDto.getCost().getAmount())
                                .tax(shoppingHistory.getSpendMoney().getTax() + createCompletedOrderDto.getCost().getTax())
                                .build());

                        completedOrder.setShoppingHistory(shoppingHistory);
                        CompletedOrder completedOrderDb = completedOrderRepository.save(completedOrder);

                        for (SoldProduct soldProduct : completedOrder.getSoldProductsList())
                        {
                            soldProduct.setCompletedOrder(completedOrder);
                            soldProductRepository.save(soldProduct);
                        }


                        shoppingHistory.getCompletedOrderList().add(completedOrderDb);
                        ShoppingHistory savedShoppingHistory = shoppingHistoryRepository.save(shoppingHistory);
                        ShoppingHistoryInfo savedShoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(savedShoppingHistory);
                        return new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), savedShoppingHistoryInfo);
                    }

                default:
                    return new ShoppingHistoryDto(new Message("There is more than one shopping history for customer!", Message.Status.FAILURE), null);
            }
        }
    }


//    @Override
//    public ShoppingHistoryDto addOrder(CreateCompletedOrderDto createCompletedOrderDto) {
//        if (!customerRepository.exists(createCompletedOrderDto.getCustomerId())) {
//            return new ShoppingHistoryDto(new Message("Customer doesn't exist", Message.Status.FAILURE), null);
//        } else {
//            List<ShoppingHistory> shoppingHistoryList = shoppingHistoryRepository.findByCustomerId(createCompletedOrderDto.getCustomerId());
//            switch (shoppingHistoryList.size()) {
//                case 0:
//                    return new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);
//
//                case 1:
//                    if (createCompletedOrderDto.getSoldProductsList().isEmpty()) {
//                        return new ShoppingHistoryDto(new Message("Delivered order doesn't contain any sold products!", Message.Status.FAILURE), null);
//                    } else {
//                        ShoppingHistory shoppingHistory = shoppingHistoryList.get(0);
//                        CompletedOrder completedOrder = shoppingHistoryMapper.mapCompletedOrder(createCompletedOrderDto);
//                        completedOrder.setSyntheticId(UUID.randomUUID().toString());
//                        // Order Validation
//
//                        shoppingHistory.setSpendMoney(Price.builder()
//                                .amount(shoppingHistory.getSpendMoney().getAmount() + createCompletedOrderDto.getCost().getAmount())
//                                .tax(shoppingHistory.getSpendMoney().getTax() + createCompletedOrderDto.getCost().getTax())
//                                .build());
//
//                        CompletedOrder completedOrderDb = completedOrderRepository.save(completedOrder);
//
//                        shoppingHistory.getCompletedOrderList().add(completedOrderDb);
//                        ShoppingHistory savedShoppingHistory = shoppingHistoryRepository.save(shoppingHistory);
//                        ShoppingHistoryInfo savedShoppingHistoryInfo = shoppingHistoryMapper.mapShoppingHistory(savedShoppingHistory);
//                        return new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), savedShoppingHistoryInfo);
//                    }
//
//                default:
//                    return new ShoppingHistoryDto(new Message("There is more than one shopping history for customer!", Message.Status.FAILURE), null);
//            }
//        }
//    }
}
