package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardCustomRepository {

    @EntityGraph(attributePaths = {"images"})
    List<BoardEntity> findAll();

    List<BoardEntity> findAllByOrderByCreateDateDesc();
    List<BoardEntity> findAllByOrderByLikeCountDesc();

    BoardEntity findById(int id);

    List<BoardEntity> findByCategoryInAndHashTagIn(List<String> categories, List<String> hashTags);
    List<BoardEntity> findByHashTagIn(List<String> hashTags);
    List<BoardEntity> findByCategoryIn(List<String> categories);
}
