package com.example.repository;

import com.example.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
  public Optional<Account> findByUsername(String username);
}
