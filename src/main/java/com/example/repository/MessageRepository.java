package com.example.repository;

import com.example.entity.Message;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Integer> {
  public List<Message> findAllByPostedBy(int postedBy);
}
