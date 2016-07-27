package com.tradeengine.ShoppingHistory.repositories;

import com.tradeengine.ShoppingHistory.entities.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoldProductRepository extends JpaRepository<SoldProduct, Long>
{
}
