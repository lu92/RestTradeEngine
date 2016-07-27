package com.tradeengine.TradeEngineAdapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LoyalityAccountDataProvider
{
    private RestTemplate restTemplate = new RestTemplate();

    private static final String URL = "pro";

    @Value("${server.port}")
    int port;

//    public LoyalityAccount loadLoyalityData(long loyalityAccountId)
//    {
//        ResponseEntity<LoyalityAccount> loyalityAccountEntity = restTemplate.getForEntity(URL + "/" + loyalityAccountId, LoyalityAccount.class);
//        LoyalityAccount loyalityAccount = loyalityAccountEntity.getBody();
//        return loyalityAccount;
//    }
}
