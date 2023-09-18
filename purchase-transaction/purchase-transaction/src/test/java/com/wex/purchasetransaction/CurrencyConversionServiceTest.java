package com.wex.purchasetransaction;

import com.wex.purchasetransaction.client.CurrencyConversionClient;
import com.wex.purchasetransaction.exception.ExchangeRateNotFound;
import com.wex.purchasetransaction.model.CurrencyConversion;
import com.wex.purchasetransaction.model.ExchangeRate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CurrencyConversionServiceTest {

    @Autowired
    private CurrencyConversionClient currencyConversionClient;

    @Test
    @DisplayName("Convert Currency Successful")
    public void testFetchExchangeRates() throws ParseException {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setTransactionId(1L);
        currencyConversion.setCountryName("Canada");
        currencyConversion.setTargetCurrency("Dollar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse("2020-09-14");
        currencyConversion.setTransactionDate(transactionDate);

        Mono<ExchangeRate> exchangeRateMono = currencyConversionClient.fetchExchangeRates(currencyConversion);

        ExchangeRate exchangeRate = exchangeRateMono.block();

        assertNotNull(exchangeRate);
        assertEquals("1.368", exchangeRate.getData().get(0).getExchangeRate());
    }

    @Test
    @DisplayName("Convert Currency Fail")
    public void testFetchExchangeRatesThrowsException() throws ParseException {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setTransactionId(1L);
        currencyConversion.setCountryName("Canada");
        currencyConversion.setTargetCurrency("Dollar");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse("1500-09-14");
        currencyConversion.setTransactionDate(transactionDate);

        assertThrows(ExchangeRateNotFound.class, () -> {
            currencyConversionClient.fetchExchangeRates(currencyConversion).block();
        });
    }

}
