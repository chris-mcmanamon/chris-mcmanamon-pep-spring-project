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
    // Validate message contents
    validateMessageContents(message);

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
  public Message deleteMessageById(int messageId) {
    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    if (optionalMessage.isPresent()) {
      Message messageToDelete = optionalMessage.get();
      messageRepository.deleteById(messageId);
      return messageToDelete;
    }
    return null;
  }

  /**
   * Update a message
   *
   * @param messageId the id of the message
   * @param newMessage a Message object containing a new messageText
   */
  public Message updateMessage(int messageId, Message newMessage) {
    // Determine if message exists
    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    if (!optionalMessage.isPresent()) {
      throw new MessageValidationException("Message not found");
    }

    // Validate the contents of the new message
    validateMessageContents(newMessage);

    // Update existing message
    Message updatedMessage = optionalMessage.get();
    updatedMessage.setMessageText(newMessage.getMessageText());
    return messageRepository.save(updatedMessage);
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

  /**
   * Helper method to validate message contents
   *
   * @param message the message to be validated
   * @throws MessageValidationException
   */
  private void validateMessageContents(Message message) throws MessageValidationException {
    if (message.getMessageText().isBlank()) {
      throw new MessageValidationException("Message is blank");
    }
    if (message.getMessageText().length() > 255) {
      throw new MessageValidationException("Message is too long");
    }
  }
}
