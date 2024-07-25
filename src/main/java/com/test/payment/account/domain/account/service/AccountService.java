package com.test.payment.account.domain.account.service;

import com.test.payment.account.domain.account.Account;
import com.test.payment.account.domain.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account account) {
        Account existingAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setDataVencimento(account.getDataVencimento());
        existingAccount.setDataPagamento(account.getDataPagamento());
        existingAccount.setValor(account.getValor());
        existingAccount.setDescricao(account.getDescricao());
        existingAccount.setSituacao(account.getSituacao());
        return accountRepository.save(existingAccount);
    }

    public Account changeAccountStatus(Long id, String situacao) {
        Account existingAccount = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        existingAccount.setSituacao(situacao);
        return accountRepository.save(existingAccount);
    }

    public Page<Account> getAccountsByDueDateAndDescription(LocalDate startDate, LocalDate endDate, String descricao, Pageable pageable) {
        return accountRepository.findByDataVencimentoBetweenAndDescricaoContaining(startDate, endDate, descricao, pageable);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public BigDecimal getTotalPaidValueByPeriod(LocalDate startDate, LocalDate endDate) {
        List<Account> accounts = accountRepository.findByDataVencimentoBetween(startDate, endDate);
        return accounts.stream()
                .filter(account -> account.getDataPagamento() != null)
                .map(Account::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
