package com.tradeengine.TradeEngine.repositories;

import com.tradeengine.TradeEngine.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>
{
    List<Category> findByName(String name);
}
