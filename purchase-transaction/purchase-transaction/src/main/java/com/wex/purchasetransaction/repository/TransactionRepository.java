package com.wex.purchasetransaction.repository;

import com.wex.purchasetransaction.model.TransactionData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionData, Long> {
}
