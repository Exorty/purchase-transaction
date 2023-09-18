package com.wex.purchasetransaction.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MetaData
{
    private int count;

    private LabelsData labels;

    private DataTypes dataTypes;

    private DataFormats dataFormats;

    @JsonProperty("total-count")
    private int totalCount;

    @JsonProperty("total-pages")
    private int totalPages;

}
