package com.test.payment.account.domain.account.controller;

import com.test.payment.account.domain.account.Account;
import com.test.payment.account.domain.account.service.AccountService;
import com.test.payment.account.domain.account.service.CsvService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Management System", description = "Operations pertaining to accounts in Account Management System")
public class AccountController {

    private final AccountService accountService;
    private final CsvService csvService;

    @Autowired
    public AccountController(AccountService accountService, CsvService csvService) {
        this.accountService = accountService;
        this.csvService = csvService;
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account in the system", tags = { "accounts" })
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account newAccount = accountService.saveAccount(account);
        return ResponseEntity.ok(newAccount);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing account", description = "Update an existing account in the system", tags = { "accounts" })
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return ResponseEntity.ok(updatedAccount);
    }

    @PatchMapping("/{id}/situacao")
    @Operation(summary = "Change the status of an existing account", description = "Change the status of an existing account in the system", tags = { "accounts" })
    public ResponseEntity<Account> changeAccountStatus(@PathVariable Long id, @RequestBody String situacao) {
        Account updatedAccount = accountService.changeAccountStatus(id, situacao);
        return ResponseEntity.ok(updatedAccount);
    }

    @GetMapping
    @Operation(summary = "View a list of available accounts", description = "View a list of available accounts filtered by due date and description", tags = { "accounts" })
    public ResponseEntity<Page<Account>> getAccountsByDueDateAndDescription(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false, defaultValue = "") String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Account> accounts = accountService.getAccountsByDueDateAndDescription(startDate, endDate, descricao, pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an account by ID", description = "Get an account by its ID", tags = { "accounts" })
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/total-paid")
    @Operation(summary = "Get the total paid value for a period", description = "Get the total paid value for a specific period", tags = { "accounts" })
    public ResponseEntity<BigDecimal> getTotalPaidValueByPeriod(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        BigDecimal totalPaid = accountService.getTotalPaidValueByPeriod(startDate, endDate);
        return ResponseEntity.ok(totalPaid);
    }

    @PostMapping("/import")
    @Operation(summary = "Import accounts from a CSV file", description = "Import accounts from a CSV file into the system", tags = { "accounts" })
    public ResponseEntity<Void> importAccountsFromCsv(@RequestParam("file") MultipartFile file) {
        csvService.importAccountsFromCsv(file);
        return ResponseEntity.ok().build();
    }
}
