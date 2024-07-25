package com.test.payment.account.domain.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.payment.account.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAccountTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        String accountJson = "{ \"data_vencimento\": \"2023-12-31\", \"data_pagamento\": \"2023-12-30\", \"valor\": 1000.0, \"descricao\": \"New Account\", \"situacao\": \"PENDENTE\" }";

        mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("New Account"));
    }

    @Test
    void updateAccountTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        String accountJson = "{ \"data_vencimento\": \"2023-12-31\", \"data_pagamento\": \"2023-12-30\", \"valor\": 500.0, \"descricao\": \"Account to Update\", \"situacao\": \"PENDENTE\" }";
        MvcResult result = mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long accountId = new ObjectMapper().readTree(responseString).get("id").asLong();

        String updateJson = "{ \"data_vencimento\": \"2023-12-31\", \"data_pagamento\": \"2023-12-30\", \"valor\": 750.0, \"descricao\": \"Updated Account\", \"situacao\": \"PAGO\" }";
        mockMvc.perform(put("/api/accounts/" + accountId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Updated Account"));
    }

    @Test
    void changeAccountStatusTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        String accountJson = "{ \"data_vencimento\": \"2023-12-31\", \"data_pagamento\": \"2023-12-30\", \"valor\": 300.0, \"descricao\": \"Account to Change Status\", \"situacao\": \"PENDENTE\" }";
        MvcResult result = mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long accountId = new ObjectMapper().readTree(responseString).get("id").asLong();

        String statusJson = "PAGO";
        mockMvc.perform(patch("/api/accounts/" + accountId + "/situacao")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.situacao").value("PAGO"));
    }

    @Test
    void getAccountsByDueDateAndDescriptionTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        mockMvc.perform(get("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31")
                        .param("descricao", "")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(10)))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists())
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void getAccountByIdTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        String accountJson = "{ \"data_vencimento\": \"2023-12-31\", \"data_pagamento\": \"2023-12-30\", \"valor\": 400.0, \"descricao\": \"Account to Get\", \"situacao\": \"PENDENTE\" }";
        MvcResult result = mockMvc.perform(post("/api/accounts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        Long accountId = new ObjectMapper().readTree(responseString).get("id").asLong();

        mockMvc.perform(get("/api/accounts/" + accountId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Account to Get"));
    }

    @Test
    void getTotalPaidValueByPeriodTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        mockMvc.perform(get("/api/accounts/total-paid")
                        .header("Authorization", "Bearer " + token)
                        .param("startDate", "2023-01-01")
                        .param("endDate", "2023-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    void importAccountsFromCsvTest() throws Exception {
        String token = obtainAccessToken("testuser", "testpassword");

        MockMultipartFile file = new MockMultipartFile("file", "accounts.csv", "text/csv",
                "id,data_vencimento,data_pagamento,valor,descricao,situacao\n1,2023-12-31,2023-12-30,1000.0,Account 1,PENDENTE\n2,2023-12-31,2023-12-30,2000.0,Account 2,PENDENTE".getBytes());

        mockMvc.perform(multipart("/api/accounts/import")
                        .file(file)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
