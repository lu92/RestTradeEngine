package com.tradeengine.ShoppingHistory.controllers;

import com.tradeengine.ShoppingHistory.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.services.ShoppingHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ShoppingHistory")
public class ShoppingHistoryController
{
    @Autowired(required = false)
    private ShoppingHistoryServiceImpl shoppingHistoryService;

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public ShoppingHistoryDto getShoppingHistory(@PathVariable("customerId") long customerId)
    {
        return shoppingHistoryService.getShoppingHistory(customerId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ShoppingHistoryDto createShoppingHistory(@RequestBody Long customerId)
    {
        return shoppingHistoryService.createShoppingHistory(customerId);
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.POST)
    @ResponseBody
    public ShoppingHistoryDto addOrderToShoppingHistory(@RequestBody CompletedOrder completedOrder,@PathVariable("customerId") long customerId)
    {
        return shoppingHistoryService.addOrder(customerId, completedOrder);
    }
}
