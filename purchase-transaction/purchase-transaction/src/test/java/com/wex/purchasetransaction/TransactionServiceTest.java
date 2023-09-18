package com.wex.purchasetransaction;

import com.wex.purchasetransaction.dto.TransactionDataDTO;
import com.wex.purchasetransaction.exception.BusinessException;
import com.wex.purchasetransaction.model.CurrencyConversion;
import com.wex.purchasetransaction.model.TransactionData;
import com.wex.purchasetransaction.model.TransactionId;
import com.wex.purchasetransaction.repository.TransactionRepository;
import com.wex.purchasetransaction.service.TransactionService;
import com.wex.purchasetransaction.validator.CurrencyConversionValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    private final CurrencyConversionValidator currencyConversionValidator = new CurrencyConversionValidator();

    @Test
    @DisplayName("Save Transaction Test")
    public void testSaveTransaction() throws ParseException {
        // Configurar dados de entrada
        TransactionDataDTO transactionDataDTO = new TransactionDataDTO();
        transactionDataDTO.setPurchaseAmountUSD(BigDecimal.valueOf(100.0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse("2020-09-14");
        transactionDataDTO.setTransactionDate(transactionDate);
        transactionDataDTO.setTransactionDescription("Purchase of a Samsung Galaxy S23");

        TransactionData transactionData = TransactionData.fromDTO(transactionDataDTO);

        TransactionData savedData = new TransactionData();
        savedData.setId(1L);

        when(transactionRepository.save(any(TransactionData.class))).thenReturn(savedData);

        TransactionId transactionId = transactionService.saveTransaction(transactionDataDTO);

        assertNotNull(transactionId);
        assertEquals(savedData.getId(), transactionId.getId());
    }


    @Test
    @DisplayName("Get Transaction test")
    public void testGetTransaction() {
        long transactionId = 1L;

        TransactionData transactionData = new TransactionData();
        transactionData.setId(transactionId);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transactionData));

        TransactionData result = transactionService.getTransaction(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
    }

    @Test
    @DisplayName("Validation Successful")
    public void testValidationSuccessful() throws ParseException {
        // Create a CurrencyConversion object and set valid values
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setCountryName("Austria");
        currencyConversion.setTargetCurrency("Euro");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse("2020-09-14");
        currencyConversion.setTransactionDate(transactionDate);
        currencyConversion.setTransactionId(1L);

        currencyConversionValidator.validate(currencyConversion);

    }

    @Test
    @DisplayName("Validation Fail")
    public void testValidationFail() throws ParseException {
        CurrencyConversion currencyConversion = new CurrencyConversion();
        currencyConversion.setCountryName(null);
        currencyConversion.setTargetCurrency("Euro");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date transactionDate = dateFormat.parse("2020-09-14");
        currencyConversion.setTransactionDate(transactionDate);
        currencyConversion.setTransactionId(1L);

        assertThrows(BusinessException.class, () -> currencyConversionValidator.validate(currencyConversion));

    }


}



