package com.tradeengine.ShoppingHistory.repositories;

import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import com.tradeengine.ShoppingHistory.entities.CompletedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedOrderRepository extends JpaRepository<CompletedOrder, Long>
{
}
