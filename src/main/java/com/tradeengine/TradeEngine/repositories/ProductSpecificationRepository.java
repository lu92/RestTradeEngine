package com.tradeengine.TradeEngine.repositories;

import com.tradeengine.TradeEngine.entities.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long>
{
}
