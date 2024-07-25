package com.test.payment.account.domain.account.service;

import com.test.payment.account.domain.account.Account;
import com.test.payment.account.domain.account.repository.AccountRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private final AccountRepository accountRepository;

    @Autowired
    public CsvService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void importAccountsFromCsv(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Account> accounts = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Account account = new Account();
                account.setDataVencimento(LocalDate.parse(csvRecord.get("data_vencimento")));
                account.setDataPagamento(LocalDate.parse(csvRecord.get("data_pagamento")));
                account.setValor(new BigDecimal(csvRecord.get("valor")));
                account.setDescricao(csvRecord.get("descricao"));
                account.setSituacao(csvRecord.get("situacao"));

                accounts.add(account);
            }

            accountRepository.saveAll(accounts);
        } catch (IOException | IllegalArgumentException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
