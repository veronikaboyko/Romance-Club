package org.example.JPA;

import org.postgresql.core.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    boolean existsByChatId(Long chatId);
    UserEntity findByChatId(Long chatId);
    @Query(nativeQuery = true,value = "select admin from users where :chatId = chatid")
    Boolean getAdmin(Long chatId);
}
