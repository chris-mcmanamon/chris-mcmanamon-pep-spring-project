package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.MessageValidationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  MessageRepository messageRepository;
  AccountRepository accountRepository;

  @Autowired
  public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
    this.messageRepository = messageRepository;
    this.accountRepository = accountRepository;
  }

  /**
   * Post a new message. Message cannot be blank or over 255 chars. User 'postedBy' must exist
   *
   * @param message a Message object without an id
   * @return the persisted Message if successful
   */
  public Message postMessage(Message message) {
    // Validate message length
    if (message.getMessageText().isBlank()) {
      throw new MessageValidationException("Message is blank");
    }
    if (message.getMessageText().length() > 255) {
      throw new MessageValidationException("Message is too long");
    }

    // Ensure user exists
    Optional<Account> account = accountRepository.findById(message.getPostedBy());
    if (!account.isPresent()) {
      throw new MessageValidationException("User not found");
    }

    // Return persisted message
    return messageRepository.save(message);
  }
}
