package com.wex.purchasetransaction.model;

import com.wex.purchasetransaction.dto.TransactionDataDTO;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transaction_data")
@Data
@NoArgsConstructor
public class TransactionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_description", length = 50)
    @Size(max = 50, message = "Transaction description must not exceed 50 characters.")
    @NotBlank(message = "Transaction description is required.")
    private String transactionDescription;

    @Temporal(TemporalType.DATE)
    @NotNull(message = "Transaction date is required.")
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Column(name = "purchase_amount")
    @NotNull(message = "Purchase amount is required.")
    private BigDecimal purchaseAmountUSD;

    public static TransactionData fromDTO(TransactionDataDTO dto) {
        TransactionData transactionData = new TransactionData();
        transactionData.setTransactionDescription(dto.getTransactionDescription());
        transactionData.setTransactionDate(dto.getTransactionDate());
        transactionData.setPurchaseAmountUSD(dto.getPurchaseAmountUSD());
        return transactionData;
    }

    public static TransactionDataDTO toDTO(TransactionData transactionData) {
        TransactionDataDTO dto = new TransactionDataDTO();
        dto.setTransactionDescription(transactionData.getTransactionDescription());
        dto.setTransactionDate(transactionData.getTransactionDate());
        dto.setPurchaseAmountUSD(transactionData.getPurchaseAmountUSD());
        dto.setId(transactionData.getId());
        return dto;
    }

}
