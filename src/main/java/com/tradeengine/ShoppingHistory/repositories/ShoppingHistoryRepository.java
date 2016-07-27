package com.tradeengine.ShoppingHistory.repositories;

import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import com.tradeengine.ShoppingHistory.entities.ShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingHistoryRepository extends JpaRepository<ShoppingHistory, Long>
{
    List<ShoppingHistory> findByCustomerId(long customerId);
}
