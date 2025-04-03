package com.example.service;

import com.example.entity.Account;
import com.example.exception.AccountValidationException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidCredentialsException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
  AccountRepository accountRepository;

  @Autowired
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * Account Registration. Username must not be blank and cannot already exist. Password must be at
   * least 4 chars long.
   *
   * @param account an account object without an Id
   * @return persisted account if successfully persisted
   */
  public Account register(Account account) {
    // Validate username and password
    if (account.getUsername().isBlank()) {
      throw new AccountValidationException("Username is blank");
    }
    if (account.getUsername().length() > 255) {
      throw new AccountValidationException("Username is too long");
    }
    if (account.getPassword().length() < 4) {
      throw new AccountValidationException("Password is too short");
    }
    if (account.getPassword().length() > 255) {
      throw new AccountValidationException("Password is too long");
    }

    // Ensure username does not already exist in permanent storage
    if (accountRepository.findByUsername(account.getUsername()) != null) {
      throw new DuplicateUsernameException();
    }

    // Persist account and return it
    return accountRepository.save(account);
  }

  /**
   * User Login. Username must exist. Password must match.
   *
   * @param account an account object
   * @return the matching account if the credentials check out
   */
  public Account login(Account account) {
    Account foundAccount = accountRepository.findByUsername(account.getUsername());
    if (foundAccount == null) {
      throw new InvalidCredentialsException("Username not found");
    }
    if (!foundAccount.getPassword().equals(account.getPassword())) {
      throw new InvalidCredentialsException("Password does not match");
    }
    return foundAccount;
  }
}
