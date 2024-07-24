package com.test.payment.account.domain.user.repository;

import com.test.payment.account.domain.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
