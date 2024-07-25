package com.test.payment.account.domain.account.repository;

import com.test.payment.account.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByDataVencimentoBetweenAndDescricaoContaining(LocalDate startDate, LocalDate endDate, String descricao);
    List<Account> findByDataVencimentoBetween(LocalDate startDate, LocalDate endDate);
}
