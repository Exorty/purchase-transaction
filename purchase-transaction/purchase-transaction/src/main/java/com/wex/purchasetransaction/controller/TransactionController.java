package com.wex.purchasetransaction.controller;

import com.wex.purchasetransaction.dto.TransactionDataDTO;
import com.wex.purchasetransaction.dto.ConvertedCurrencyDTO;
import com.wex.purchasetransaction.model.CurrencyConversion;
import com.wex.purchasetransaction.model.TransactionData;
import com.wex.purchasetransaction.model.TransactionId;
import com.wex.purchasetransaction.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("wex")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/transaction/add")
    @ApiOperation(value = "Store a Purchase Transaction", notes = "Stores a purchase transaction with a description, transaction date, and purchase amount in United States dollars.")
    @ApiResponse(code = 201, message = "Transaction created")
    public ResponseEntity<TransactionId> saveTransaction(@RequestBody TransactionDataDTO transactionDataDTO) {
        TransactionId transactionId = service.saveTransaction(transactionDataDTO);

        return new ResponseEntity<>(transactionId, HttpStatus.CREATED);
    }

    @GetMapping("/transaction/{id}")
    @ApiOperation(value = "Retrieve a Purchase Transaction", notes = "Retrieve a purchase transaction by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction found and returned successfully"),
            @ApiResponse(code = 404, message = "Transaction not found")
    })
    public ResponseEntity<TransactionData> getTransaction(@PathVariable("id") Long transactionId) {

        TransactionData transaction = service.getTransaction(transactionId);

        return new ResponseEntity<>(transaction, HttpStatus.OK);


    }

    @GetMapping("/transactions")
    @ApiOperation(value = "Retrieve All Purchase Transactions", notes = "Retrieve all purchase transactions.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transactions found and returned successfully")
    })
    public ResponseEntity<List<TransactionDataDTO>> getAllTransactions() {
        List<TransactionDataDTO> transactions = service.getAllTransaction();

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PostMapping("/transaction/currency/convert")
    @ApiOperation(value = "Retrieve Purchase Transactions by Currency",
            notes = "Retrieves purchase transactions converted to the specified currency based on exchange rates.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transactions converted successfully"),
            @ApiResponse(code = 404, message = "No transactions found for the given currency")
    })
    public Mono<ResponseEntity<ConvertedCurrencyDTO>> retrievePurchaseTransactionsByCurrency(@RequestBody CurrencyConversion currencyConversion) {

        return service.retrievePurchaseTransactionsByCurrency(currencyConversion)
                .map(convertedCurrencyDTO -> new ResponseEntity<>(convertedCurrencyDTO, HttpStatus.OK));
    }

}
