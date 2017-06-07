package com.galvanize.Repository;

import com.galvanize.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRespository extends CrudRepository<User, Integer> {

    User findById(Integer id);

    User findByEmail(String email);

    @Modifying
    @Query("update User u set u.email = ?1 where u.id = ?2")
    @Transactional
    Integer updateUserEmailbyId(String email, Integer userId);


    @Modifying
    @Query("update User u set u.email = ?1 , u.password =?2 where u.id = ?3")
    @Transactional
    Integer updateUserEmailAndPasswordbyId(String email, String password, Integer userId);

}
