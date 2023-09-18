package com.wex.purchasetransaction.model;

import lombok.Data;

import java.util.List;

@Data
public class ExchangeRate {

    private List<ExchangeRateData> data;

    private MetaData meta;

    private LinksData links;

}
