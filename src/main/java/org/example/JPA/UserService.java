package org.example.JPA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    public void save(UserEntity entity){
        repo.save(entity);
    }
    public boolean existsByChatId(Long chatId){
        System.out.println(chatId);
        return repo.existsByChatId(chatId);
    }
    public UserEntity inTable (Long chatId){
        return repo.findByChatId(chatId);
    }
    public boolean check(Long chatId){
        return repo.getAdmin(chatId);
    }
}
