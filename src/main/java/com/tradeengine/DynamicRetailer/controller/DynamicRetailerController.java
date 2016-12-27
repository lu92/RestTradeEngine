package com.tradeengine.DynamicRetailer.controller;

import com.tradeengine.DynamicRetailer.dto.RuleListDto;
import com.tradeengine.DynamicRetailer.services.DynamicRetailerServiceImpl;
import com.tradeengine.TradeEngineAdapter.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/DynamicRetailer")
public class DynamicRetailerController
{
    @Autowired
    private DynamicRetailerServiceImpl dynamicRetailerService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RuleListDto getRules() {
        return dynamicRetailerService.getRules();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Order calculateDiscount(@RequestBody Order order) {
        return dynamicRetailerService.calculateDiscount(order);
    }
}
