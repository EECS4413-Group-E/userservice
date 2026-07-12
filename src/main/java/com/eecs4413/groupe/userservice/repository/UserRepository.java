package com.eecs4413.groupe.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eecs4413.groupe.userservice.model.entity.User;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Transactional
    @Query("SELECT exists(SELECT u from User u where u.email = ?1)")
    boolean existsByEmail(String email);

}
