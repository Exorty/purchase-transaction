package com.wex.purchasetransaction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ConvertedCurrencyDTO {

    private Long transactionId;

    private String description;

    private Date transactionDate;

    private BigDecimal originalAmountUSD;

    private double exchangeRate;

    private BigDecimal targetCurrencyAmount;

}
