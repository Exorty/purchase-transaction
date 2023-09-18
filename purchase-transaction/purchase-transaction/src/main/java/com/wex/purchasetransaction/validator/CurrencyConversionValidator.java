package com.wex.purchasetransaction.validator;

import com.wex.purchasetransaction.exception.BusinessException;
import com.wex.purchasetransaction.model.CurrencyConversion;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CurrencyConversionValidator {

    public void validate(CurrencyConversion request) {

        if (isNullOrEmpty(request)) {
            throw new BusinessException("Currency conversion request is null or empty.");
        }

        if (isNullOrEmpty(request.getCountryName())) {
            throw new BusinessException("Country name is null or empty.");
        }

        if (isNullOrEmpty(request.getTargetCurrency())) {
            throw new BusinessException("Target currency is null or empty.");
        }

        if (isNullOrEmpty(request.getTransactionDate())) {
            throw new BusinessException("Transaction date is null or empty.");
        }

        if (isNullOrEmpty(request.getTransactionId())) {
            throw new BusinessException("Transaction ID is null or empty.");
        }

    }

    private boolean isNullOrEmpty(Object obj) {
        return Objects.isNull(obj) || String.valueOf(obj).trim().equals("");
    }

}
