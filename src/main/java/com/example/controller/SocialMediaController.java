package com.example.controller;

import com.example.entity.Account;
import com.example.exception.AccountValidationException;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring.
 * The endpoints you will need can be found in readme.md as well as the test cases. You be required
 * to use the @GET/POST/PUT/DELETE/etc Mapping annotations where applicable as well as
 * the @ResponseBody and @PathVariable annotations. You should refer to prior mini-project labs and
 * lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
  AccountService accountService;
  MessageService messageService;

  @Autowired
  public SocialMediaController(AccountService accountService, MessageService messageService) {
    this.accountService = accountService;
    this.messageService = messageService;
  }

  /**
   * Endpoint for registering a new account
   *
   * @param account an account in JSON format in the RequestBody
   * @return the persisted account with status code 200 OK
   */
  @PostMapping("/register")
  public ResponseEntity<Account> registerAccount(@RequestBody Account account) {
    Account persistedAccount = accountService.register(account);
    return ResponseEntity.status(HttpStatus.OK).body(persistedAccount);
  }

  /**
   * Handles exceptions when attempting to register an existing username
   *
   * @param e the exception
   * @return 409 CONFLICT and the exception message
   */
  @ExceptionHandler(DuplicateUsernameException.class)
  public ResponseEntity<String> handleDuplicateUsername(DuplicateUsernameException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }

  /**
   * Handles exceptions when an account does not meet specifications
   *
   * @param e the exception
   * @return 400 Bad Request and the exception message
   */
  @ExceptionHandler(AccountValidationException.class)
  public ResponseEntity<String> handleAccountInvalid(AccountValidationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }
}
