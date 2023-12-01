package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardCustomRepository {

    @EntityGraph(attributePaths = {"images"})
    List<BoardEntity> findAll();

    List<BoardEntity> findAllByOrderByCreateDateDesc();
    @Query("SELECT b FROM BoardEntity b LEFT JOIN LikeEntity l ON b.boardId = l.boardId.boardId GROUP BY b.boardId ORDER BY COUNT(l) DESC")
    List<BoardEntity> findAllByOrderByLikeCountDesc();

    BoardEntity findById(int id);

    List<BoardEntity> findByNickName(String nickName);

}
