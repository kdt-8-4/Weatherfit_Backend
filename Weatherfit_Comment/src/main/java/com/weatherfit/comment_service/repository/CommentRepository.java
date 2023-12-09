package com.weatherfit.comment_service.repository;

import com.weatherfit.comment_service.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @EntityGraph(attributePaths = "replyList")
    List<Comment> findByBoardId(int boardId);
}
