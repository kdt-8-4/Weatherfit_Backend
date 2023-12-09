package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>, BoardCustomRepository {

    @EntityGraph(attributePaths = {"images"})
    List<BoardEntity> findAll();

    List<BoardEntity> findAllByOrderByCreateDateDesc();

    @Query("SELECT b FROM BoardEntity b LEFT JOIN LikeEntity l ON b.boardId = l.boardId.boardId GROUP BY b.boardId ORDER BY COUNT(l) DESC")
    List<BoardEntity> findAllByOrderByLikeCountDesc();

    BoardEntity findById(int id);

    List<BoardEntity> findByNickName(String nickName);

    @Query("SELECT b FROM BoardEntity b WHERE b.temperature >= :minTemp AND b.temperature <= :maxTemp ORDER BY size(b.likelist) DESC")
    List<BoardEntity> findBoardsByTemperatureRange(@Param("minTemp") double minTemp, @Param("maxTemp") double maxTemp);
}
