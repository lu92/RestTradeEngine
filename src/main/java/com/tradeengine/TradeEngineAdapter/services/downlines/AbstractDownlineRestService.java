package com.tradeengine.TradeEngineAdapter.services.downlines;


import lombok.Getter;
import org.springframework.web.client.RestTemplate;


public abstract class AbstractDownlineRestService {

    protected @Getter int PORT;
    protected @Getter String HOST_ADDRESS;
    protected @Getter String DOWNLINE_NAME;
    protected @Getter String DOWNLINE_BASE_URL;
    protected RestTemplate restTemplate;

    protected AbstractDownlineRestService(int PORT, String HOST_ADDRESS, String DOWNLINE_NAME) {
        this.PORT = PORT;
        this.HOST_ADDRESS = HOST_ADDRESS;
        this.DOWNLINE_NAME = DOWNLINE_NAME;
        this.restTemplate = new RestTemplate();
        this.DOWNLINE_BASE_URL = "http://" + HOST_ADDRESS + ":" + PORT + "/" + DOWNLINE_NAME;
    }
}
