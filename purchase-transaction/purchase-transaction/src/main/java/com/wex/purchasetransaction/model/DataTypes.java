package com.wex.purchasetransaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DataTypes {

    @JsonProperty("record_date")
    private String recordDate;

    private String country;

    private String currency;

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDesc;

    @JsonProperty("exchange_rate")
    private String exchangeRate;

    @JsonProperty("effective_date")
    private String effectiveDate;

    @JsonProperty("src_line_nbr")
    private String srcLineNbr;

    @JsonProperty("record_fiscal_year")
    private String recordFiscalYear;

    @JsonProperty("record_fiscal_quarter")
    private String recordFiscalQuarter;

    @JsonProperty("record_calendar_year")
    private String recordCalendarYear;

    @JsonProperty("record_calendar_quarter")
    private String recordCalendarQuarter;

    @JsonProperty("record_calendar_month")
    private String recordCalendarMonth;

    @JsonProperty("record_calendar_day")
    private String recordCalendarDay;

}
