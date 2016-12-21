package com.tradeengine.TradeEngineAdapter.end2endTest;

import com.tradeengine.TestUtils;
import com.tradeengine.TradeEngine.dto.CategoryDto;
import com.tradeengine.TradeEngine.dto.CreateCategoryDto;
import com.tradeengine.TradeEngineAdapter.services.adapter.TradeEngineGateway;
import com.tradeengine.TradeEngineAdapter.services.downlines.BasketRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ProfileReaderRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.ShoppingHistoryRestService;
import com.tradeengine.TradeEngineAdapter.services.downlines.TradeEngineRestService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.logging.Logger;

import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_CATEGORY_NAME;
import static com.tradeengine.TradeEngine.TradeEngineTestData.PHONES_PRODUCT_SCHEME;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryStructureTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    // Application must be started

    private ProfileReaderRestService profileReaderRestService = new ProfileReaderRestService();
    private TradeEngineRestService tradeEngineRestService = new TradeEngineRestService();
    private ShoppingHistoryRestService shoppingHistoryRestService = new ShoppingHistoryRestService();
    private BasketRestService basketRestService =
            new BasketRestService(profileReaderRestService, tradeEngineRestService, shoppingHistoryRestService);

    private TradeEngineGateway tradeEngineGateway =
            new TradeEngineGateway(profileReaderRestService, tradeEngineRestService, shoppingHistoryRestService, basketRestService);

    private static CategoryDto categoryDto;

    @Test
    public void _2_createCategory() throws IOException {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto(PHONES_CATEGORY_NAME, null, PHONES_PRODUCT_SCHEME);
        logger.info(TestUtils.convertObjectToJsonText(createCategoryDto));

        categoryDto = tradeEngineGateway.createCategory(createCategoryDto);
        logger.info(TestUtils.convertObjectToJsonText(categoryDto));
    }
}
