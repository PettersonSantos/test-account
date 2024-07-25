package com.test.payment.account.domain.account.service;

import com.test.payment.account.domain.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CsvServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private CsvService csvService;

    @Test
    void testImportAccountsFromCsv() throws IOException {
        String csvContent = "data_vencimento,data_pagamento,valor,descricao,situacao\n"
                + "2023-12-31,2023-12-30,1000.0,Account 1,PENDENTE\n"
                + "2023-12-31,2023-12-30,2000.0,Account 2,PENDENTE";
        MockMultipartFile file = new MockMultipartFile("file", "accounts.csv", "text/csv", csvContent.getBytes());

        csvService.importAccountsFromCsv(file);

        verify(accountRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testImportAccountsFromCsv_withInvalidCsv() {
        String csvContent = "invalid_column,data_pagamento,valor,descricao,situacao\n"
                + "2023-12-31,2023-12-30,1000.0,Account 1,PENDENTE\n"
                + "2023-12-31,2023-12-30,2000.0,Account 2,PENDENTE";
        MockMultipartFile file = new MockMultipartFile("file", "accounts.csv", "text/csv", csvContent.getBytes());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            csvService.importAccountsFromCsv(file);
        });

        assertTrue(exception.getMessage().contains("Failed to parse CSV file"));
    }
}
