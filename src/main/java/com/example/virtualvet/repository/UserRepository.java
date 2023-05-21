package com.example.virtualvet.repository;

import com.example.virtualvet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

//    @Query("select * from users where merulkowiczko = ':siur'") //todo wywal, dalem jako przyklad jak tez mozna jak sie juz springowym nie da
//    User findByMerulko(String siur);

}
