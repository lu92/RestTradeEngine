package com.tradeengine.TradeEngine.dto;

import com.tradeengine.common.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListDto
{
    private Message message;
    private List<CategoryElement> categoryList;

    public List<CategoryElement> getCategoryList() {
        if (categoryList == null)
            categoryList = new ArrayList<>();

        return categoryList;
    }
}
