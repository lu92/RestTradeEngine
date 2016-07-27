package com.tradeengine.TradeEngine.repositories;

import com.tradeengine.TradeEngine.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>
{
    List<Product> findByCommercialName(String commercialName);
//    List<Product> findByCategory(String category);
}
