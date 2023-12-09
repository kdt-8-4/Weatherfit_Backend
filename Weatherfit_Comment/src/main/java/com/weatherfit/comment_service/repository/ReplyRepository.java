package com.weatherfit.comment_service.repository;

import com.weatherfit.comment_service.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

}
