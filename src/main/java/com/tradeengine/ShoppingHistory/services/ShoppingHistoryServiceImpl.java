package com.tradeengine.ShoppingHistory.services;

import com.tradeengine.ProfileReader.repositories.CustomerRepository;
import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.repositories.CompletedOrderRepository;
import com.tradeengine.ShoppingHistory.repositories.ShoppingHistoryRepository;
import com.tradeengine.common.Message;
import com.tradeengine.common.entities.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ShoppingHistoryServiceImpl implements ShoppingHistoryService
{
    @Autowired
    private ShoppingHistoryRepository shoppingHistoryRepository;

    @Autowired
    private CompletedOrderRepository completedOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ShoppingHistoryDto getShoppingHistory(long customerId)
    {
        List<ShoppingHistory> shoppingHistoryList = shoppingHistoryRepository.findByCustomerId(customerId);
        switch (shoppingHistoryList.size())
        {
            case 0:
                return new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

            case 1:
                return new ShoppingHistoryDto(new Message("Shopping history has been delivered!", Message.Status.SUCCESS), shoppingHistoryList.get(0));

            default:
                return new ShoppingHistoryDto(new Message("There is more than one shopping history for customer!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public ShoppingHistoryDto createShoppingHistory(long customerId)
    {
        if (shoppingHistoryRepository.findByCustomerId(customerId).isEmpty())
        {
            ShoppingHistory shoppingHistory = ShoppingHistory.builder().customerId(customerId).syntheticId(UUID.randomUUID().toString())
                    .spendMoney(Price.builder().amount(0).tax(0).build()).build();
            ShoppingHistory shoppingHistoryDb = shoppingHistoryRepository.save(shoppingHistory);
            return new ShoppingHistoryDto(new Message("Shopping history has been created!", Message.Status.SUCCESS), shoppingHistoryDb);
        }
        else
        {
            return new ShoppingHistoryDto(new Message("Shopping history already exists!", Message.Status.FAILURE), null);
        }
    }

    @Override
    public ShoppingHistoryDto addOrder(long customerId, CompletedOrder completedOrder)
    {
        if (!customerRepository.exists(customerId))
        {
            return new ShoppingHistoryDto(new Message("Customer doesn't exist", Message.Status.FAILURE), null);
        }
        else
        {
            List<ShoppingHistory> shoppingHistoryList = shoppingHistoryRepository.findByCustomerId(customerId);
            switch (shoppingHistoryList.size())
            {
                case 0:
                    return new ShoppingHistoryDto(new Message("Shopping history doesn't exist for selected customer!", Message.Status.FAILURE), null);

                case 1:
                    if (completedOrder.getSoldProductsList().isEmpty())
                    {
                        return new ShoppingHistoryDto(new Message("Delivered order doesn't contain any product's id!", Message.Status.FAILURE), null);
                    }
                    else
                    {
                        ShoppingHistory shoppingHistory = shoppingHistoryList.get(0);
                        completedOrder.setSyntheticId(UUID.randomUUID().toString());
                        // Order Validation

                        shoppingHistory.setSpendMoney(Price.builder()
                                .amount(shoppingHistory.getSpendMoney().getAmount() + completedOrder.getCost().getAmount())
                                .tax(shoppingHistory.getSpendMoney().getTax() + completedOrder.getCost().getTax())
                                .build());
//                        shoppingHistory.setTotalAmount(shoppingHistory.getTotalAmount() + completedOrder.getCost().getAmount());
//                        shoppingHistory.setTotalTaxes(shoppingHistory.getTotalTaxes() + completedOrder.getCost().getTax());

                        CompletedOrder completedOrderDb = completedOrderRepository.save(completedOrder);

                        //                    completedOrder.setShoppingHistory(shoppingHistory);
                        shoppingHistory.getCompletedOrderList().add(completedOrderDb);
                        ShoppingHistory savedShoppingHistory = shoppingHistoryRepository.save(shoppingHistory);
                        return new ShoppingHistoryDto(new Message("Order has been added to shopping history!", Message.Status.SUCCESS), savedShoppingHistory);
                    }

                default:
                    return new ShoppingHistoryDto(new Message("There is more than one shopping history for customer!", Message.Status.FAILURE), null);
            }
        }
    }
}
