package com.wex.purchasetransaction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDataDTO {

    private Long id;

    private String transactionDescription;

    private Date transactionDate;

    private BigDecimal purchaseAmountUSD;

}
