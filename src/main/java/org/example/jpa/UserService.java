package org.example.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * класс для работы с пользователем в базе данных.
 */
@Service
@RequiredArgsConstructor
public class UserService {
  @Autowired private UserRepo repo;
  private final StatEntityRespository respository;
  public void save(UserEntity entity, Long chatId) {
    if (entity.getId() != null) {
      Optional<UserEntity> u = repo.findById(entity.getId());
      if (u.isPresent()) {
        UserEntity u1 = u.get();
        if (u1.getState() != entity.getState()) {
          respository.save(StatEntity.builder().state(entity.getState()).build());
        }
      }
    }
    repo.save(entity);
  }

  public boolean existsByChatId(Long chatId) {
    return repo.existsByChatId(chatId);
  }

  public UserEntity inTable(Long chatId) {
    return repo.findByChatId(chatId);
  }

  public boolean check(Long chatId) {
    return repo.getAdmin(chatId);
  }
}
