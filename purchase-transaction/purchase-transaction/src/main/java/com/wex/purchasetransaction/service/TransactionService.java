package com.wex.purchasetransaction.service;

import com.wex.purchasetransaction.client.CurrencyConversionClient;
import com.wex.purchasetransaction.dto.ConvertedCurrencyDTO;
import com.wex.purchasetransaction.dto.TransactionDataDTO;
import com.wex.purchasetransaction.exception.ExchangeRateNotFound;
import com.wex.purchasetransaction.exception.TransactionNotFoundException;
import com.wex.purchasetransaction.model.*;
import com.wex.purchasetransaction.repository.TransactionRepository;
import com.wex.purchasetransaction.validator.CurrencyConversionValidator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;
    private final CurrencyConversionClient currencyConversionClient;
    private final CurrencyConversionValidator currencyConversionValidator;


    public TransactionService(TransactionRepository repository,
                              CurrencyConversionClient currencyConversionClient,
                              CurrencyConversionValidator currencyConversionValidator) {
        this.repository = repository;
        this.currencyConversionClient = currencyConversionClient;
        this.currencyConversionValidator = currencyConversionValidator;
    }

    public TransactionId saveTransaction(TransactionDataDTO transactionDataDTO) {
        TransactionData transactionData = new TransactionData().fromDTO(transactionDataDTO);

        BigDecimal purchaseAmount = validateAndRoundPurchaseAmount(transactionData.getPurchaseAmountUSD());
        transactionData.setPurchaseAmountUSD(purchaseAmount);
        TransactionData savedData = repository.save(transactionData);

        return new TransactionId(savedData.getId());
    }

    public TransactionData getTransaction(Long transactionId) {
        return repository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with ID " + transactionId + " not found"));
    }

    public List<TransactionDataDTO> getAllTransaction() {
        List<TransactionData> transactions = repository.findAll();
        List<TransactionDataDTO> transactionDTOs = new ArrayList<>();

        for (TransactionData transaction : transactions) {
            TransactionDataDTO dto = TransactionData.toDTO(transaction);
            transactionDTOs.add(dto);
        }

        return transactionDTOs;
    }

    public Mono<ConvertedCurrencyDTO> retrievePurchaseTransactionsByCurrency(CurrencyConversion currencyConversion) {

        currencyConversionValidator.validate(currencyConversion);

        TransactionData transaction = getTransaction(currencyConversion.getTransactionId());

        return currencyConversionClient.fetchExchangeRates(currencyConversion)
                .flatMap(exchangeRate -> {
                    System.out.println();
                    ConvertedCurrencyDTO convertedCurrencyDTO = buildConvertedCurrency(transaction, exchangeRate);

                    return Mono.just(convertedCurrencyDTO);

                })
                .onErrorResume(ExchangeRateNotFound.class, error -> {

                    return Mono.error(error);
                });

    }

    private ConvertedCurrencyDTO buildConvertedCurrency(TransactionData transaction, ExchangeRate exchangeRate) {
        ConvertedCurrencyDTO convertedCurrencyDTO = new ConvertedCurrencyDTO();
        convertedCurrencyDTO.setTransactionId(transaction.getId());
        convertedCurrencyDTO.setTransactionDate(transaction.getTransactionDate());
        convertedCurrencyDTO.setDescription(transaction.getTransactionDescription());
        convertedCurrencyDTO.setOriginalAmountUSD(transaction.getPurchaseAmountUSD());
        convertedCurrencyDTO.setExchangeRate(Double.parseDouble(exchangeRate.getData().get(0).getExchangeRate()));


        BigDecimal targetCurrencyAmount = calculateTargetCurrencyAmount(convertedCurrencyDTO.getOriginalAmountUSD(), convertedCurrencyDTO.getExchangeRate());

        convertedCurrencyDTO.setTargetCurrencyAmount(targetCurrencyAmount);

        return convertedCurrencyDTO;
    }

    private BigDecimal calculateTargetCurrencyAmount(BigDecimal originalAmountUSD, double exchangeRate) {
        BigDecimal targetCurrencyAmount = originalAmountUSD.multiply(BigDecimal.valueOf(exchangeRate));
        targetCurrencyAmount = targetCurrencyAmount.setScale(2, RoundingMode.HALF_UP);

        return targetCurrencyAmount;
    }

    private BigDecimal validateAndRoundPurchaseAmount(BigDecimal purchaseAmount) {

        if (purchaseAmount == null || purchaseAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Purchase amount must be a valid positive amount.");
        }

        return purchaseAmount.setScale(2, RoundingMode.HALF_UP);
    }

}



