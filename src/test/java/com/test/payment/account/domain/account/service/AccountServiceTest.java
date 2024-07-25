package com.test.payment.account.domain.account.service;

import com.test.payment.account.domain.account.Account;
import com.test.payment.account.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setDataVencimento(LocalDate.of(2023, 12, 31));
        account.setDataPagamento(LocalDate.of(2023, 12, 30));
        account.setValor(new BigDecimal("1000.0"));
        account.setDescricao("Test Account");
        account.setSituacao("PENDENTE");
    }

    @Test
    void testSaveAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account savedAccount = accountService.saveAccount(account);

        assertNotNull(savedAccount);
        assertEquals("Test Account", savedAccount.getDescricao());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testUpdateAccount() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.updateAccount(1L, account);

        assertNotNull(updatedAccount);
        assertEquals("Test Account", updatedAccount.getDescricao());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testChangeAccountStatus() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account updatedAccount = accountService.changeAccountStatus(1L, "PAGO");

        assertNotNull(updatedAccount);
        assertEquals("PAGO", updatedAccount.getSituacao());
        verify(accountRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testGetAccountsByDueDateAndDescription() {
        when(accountRepository.findByDataVencimentoBetweenAndDescricaoContaining(any(LocalDate.class), any(LocalDate.class), anyString()))
                .thenReturn(Arrays.asList(account));

        List<Account> accounts = accountService.getAccountsByDueDateAndDescription(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), "Test");

        assertNotNull(accounts);
        assertEquals(1, accounts.size());
        verify(accountRepository, times(1))
                .findByDataVencimentoBetweenAndDescricaoContaining(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31), "Test");
    }

    @Test
    void testGetAccountById() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(1L);

        assertNotNull(foundAccount);
        assertEquals("Test Account", foundAccount.getDescricao());
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTotalPaidValueByPeriod() {
        Account paidAccount1 = new Account();
        paidAccount1.setDataVencimento(LocalDate.of(2023, 12, 31));
        paidAccount1.setDataPagamento(LocalDate.of(2023, 12, 30));
        paidAccount1.setValor(new BigDecimal("500.0"));
        paidAccount1.setDescricao("Paid Account 1");
        paidAccount1.setSituacao("PAGO");

        Account paidAccount2 = new Account();
        paidAccount2.setDataVencimento(LocalDate.of(2023, 12, 31));
        paidAccount2.setDataPagamento(LocalDate.of(2023, 12, 30));
        paidAccount2.setValor(new BigDecimal("1000.0"));
        paidAccount2.setDescricao("Paid Account 2");
        paidAccount2.setSituacao("PAGO");

        Account unpaidAccount = new Account();
        unpaidAccount.setDataVencimento(LocalDate.of(2023, 12, 31));
        unpaidAccount.setValor(new BigDecimal("500.0"));
        unpaidAccount.setDescricao("Unpaid Account");
        unpaidAccount.setSituacao("PENDENTE");

        when(accountRepository.findByDataVencimentoBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Arrays.asList(paidAccount1, paidAccount2, unpaidAccount));

        BigDecimal totalPaid = accountService.getTotalPaidValueByPeriod(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        assertNotNull(totalPaid);
        assertEquals(new BigDecimal("1500.0"), totalPaid);
        verify(accountRepository, times(1)).findByDataVencimentoBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
    }
}