package com.wex.purchasetransaction.client;


import com.wex.purchasetransaction.exception.ExchangeRateNotFound;
import com.wex.purchasetransaction.model.CurrencyConversion;
import com.wex.purchasetransaction.model.ExchangeRate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class CurrencyConversionClient {

    private final WebClient webClient;

    public CurrencyConversionClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.fiscaldata.treasury.gov/services/api/fiscal_service/").build();
    }

    public Mono<ExchangeRate> fetchExchangeRates(CurrencyConversion currencyConversion) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("v1/accounting/od/rates_of_exchange")
                        .queryParam("filter", "country:eq:" + formatCountryName(currencyConversion.getCountryName())
                                + ",currency:eq:" + formatTargetCurrencyCode(currencyConversion.getTargetCurrency())
                                + ",record_date:gte:" + getSixMonthsAgo(currencyConversion.getTransactionDate())
                                + ",record_date:lte:" + formatDate(currencyConversion.getTransactionDate()))
                        .queryParam("sort", "-record_calendar_year,-record_calendar_month")
                        .build())
                .retrieve()
                .bodyToMono(ExchangeRate.class)
                .flatMap(exchangeRate -> {
                    if (exchangeRate != null && exchangeRate.getMeta().getTotalCount() > 0) {

                        return Mono.just(exchangeRate);

                    } else {
                        return Mono.error(new ExchangeRateNotFound("No currency conversion rate is available within 6 months equal to or before the purchase date. The purchase cannot be converted to the target currency."));

                    }
                });

    }

    private String formatCountryName(String countryName) {

        return countryName.substring(0, 1).toUpperCase() + countryName.substring(1).toLowerCase();
    }

    private String formatTargetCurrencyCode(String targetCurrencyCode) {

        return targetCurrencyCode.substring(0, 1).toUpperCase() + targetCurrencyCode.substring(1).toLowerCase();
    }

    public String getSixMonthsAgo(Date transactionDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transactionDate);
        calendar.add(Calendar.MONTH, -6);

        Date sixMonthsAgoDate = calendar.getTime();
        String formattedSixMonthsAgo = formatDate(sixMonthsAgoDate);

        return formattedSixMonthsAgo;
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(date);

        return formattedDate;
    }

}
