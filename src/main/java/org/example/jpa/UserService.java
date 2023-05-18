package org.example.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * класс для работы с пользователем в базе данных.
 */
@Service
public class UserService {
  @Autowired private UserRepo repo;

  public void save(UserEntity entity, Long chatId) {
    repo.findByChatId(chatId);
    repo.save(entity);
  }

  public boolean existsByChatId(Long chatId) {
    System.out.println(chatId);
    return repo.existsByChatId(chatId);
  }

  public UserEntity inTable(Long chatId) {
    return repo.findByChatId(chatId);
  }

  public boolean check(Long chatId) {
    return repo.getAdmin(chatId);
  }
}
