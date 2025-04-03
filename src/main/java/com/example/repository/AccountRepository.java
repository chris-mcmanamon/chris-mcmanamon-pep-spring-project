package com.example.repository;

import com.example.entity.Account;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
  public Optional<Account> findByUsername(String username);
}
