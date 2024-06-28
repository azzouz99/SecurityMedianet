package com.example.securitymedianet.Repositories;

import com.example.securitymedianet.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE size(u.roles) = 1 AND r.name = 'USER'")
    List<User> findUsersWithOnlyUserRole();




}