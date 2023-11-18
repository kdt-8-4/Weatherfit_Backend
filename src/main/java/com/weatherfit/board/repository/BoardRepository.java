package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {


    List<BoardEntity> findAllByOrderByCreateDateDesc();
    List<BoardEntity> findAllByOrderByLikeCountDesc();

    BoardEntity findById(int id);


}
