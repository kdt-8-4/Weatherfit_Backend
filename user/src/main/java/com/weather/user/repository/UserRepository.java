package com.weather.user.repository;

import com.weather.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select m from User m where m.fromSocial = :social and m.email = :email")
    Optional<User> findByEmail(@Param("email") String email, @Param("social") boolean social);


    @Query("select m from User m where m.email = :email")
    Optional<User> findByEmail(@Param("email") String email);
}
