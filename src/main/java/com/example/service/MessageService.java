package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.MessageValidationException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import java.util.List;
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

  /**
   * Retrieve all messages
   *
   * @return a list of messages
   */
  public List<Message> getAllMessages() {
    return (List<Message>) messageRepository.findAll();
  }

  /**
   * Retrieve message by messageId
   *
   * @param messageId the id of the message
   * @return the message matching the given id
   */
  public Message getMessageById(int messageId) {
    Optional<Message> message = messageRepository.findById(messageId);
    if (message.isPresent()) {
      return message.get();
    } else {
      return null;
    }
  }

  /**
   * Delete a message by messageId
   *
   * @param messageId the id of the message
   * @return the number of rows affected
   */
  // TODO: Decide whether Delete and Update should return the deleted/updated object for consistency
  //
  public int deleteMessageById(int messageId) {
    if (messageRepository.existsById(messageId)) {
      messageRepository.deleteById(messageId);
      return 1;
    }
    return 0;
  }

  /**
   * Update a message
   *
   * @param messageId the id of the message
   * @param newMessage a Message object containing a new messageText
   */
  public void updateMessage(int messageId, Message newMessage) {
    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    if (!optionalMessage.isPresent()) {
      throw new MessageValidationException("Message not found");
    }

    // TODO: helper function for message validation
    if (newMessage.getMessageText().isBlank()) {
      throw new MessageValidationException("Message is blank");
    }
    if (newMessage.getMessageText().length() > 255) {
      throw new MessageValidationException("Message is too long");
    }

    Message updatedMessage = optionalMessage.get();
    updatedMessage.setMessageText(newMessage.getMessageText());
    messageRepository.save(updatedMessage);
  }

  /**
   * Get all messages by user
   *
   * @param accountId the Id of the user
   * @return a list of messages posted by the user
   */
  public List<Message> getMessagesByUser(int accountId) {
    return messageRepository.findAllByPostedBy(accountId);
  }
}
