package com.tradeengine.ShoppingHistory.controllers;

import com.tradeengine.ShoppingHistory.dto.CreateCompletedOrderDto;
import com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto;
import com.tradeengine.ShoppingHistory.services.ShoppingHistoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ShoppingHistory")
public class ShoppingHistoryController {
    @Autowired(required = false)
    private ShoppingHistoryServiceImpl shoppingHistoryService;

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    @ResponseBody
    public com.tradeengine.ShoppingHistory.dto.ShoppingHistoryDto getShoppingHistory(@PathVariable("customerId") long customerId) {
        return shoppingHistoryService.getShoppingHistory(customerId);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ShoppingHistoryDto createShoppingHistory(@RequestBody Long customerId) {
        return shoppingHistoryService.createShoppingHistory(customerId);
    }

    @RequestMapping(value = "/Orders", method = RequestMethod.POST)
    @ResponseBody
    public ShoppingHistoryDto addOrderToShoppingHistory(@RequestBody CreateCompletedOrderDto createCompletedOrderDto) {
        return shoppingHistoryService.addOrder(createCompletedOrderDto);
    }
}
