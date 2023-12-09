package com.weather.user.repository;

import com.weather.user.entity.MailCode;
import com.weather.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MailCodeRepository extends JpaRepository<MailCode, String> {
    @Query("select m from MailCode m where m.email = :email")
    Optional<MailCode> findByEmail(@Param("email") String email);
}
