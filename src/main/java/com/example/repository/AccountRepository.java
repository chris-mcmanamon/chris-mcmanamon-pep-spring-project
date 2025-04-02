package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Integer> {
  public Account findByUsername(String username);
}
