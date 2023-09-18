package com.wex.purchasetransaction.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class CurrencyConversion {

    @NotNull(message = "Transaction id is required.")
    private Long transactionId;

    @NotNull(message = "Country name is required.")
    private String countryName;

    @NotNull(message = "Target currency is required.")
    private String targetCurrency;

    @NotNull(message = "Transaction date is required.")
    private Date transactionDate;

}
