package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    int countByBoardId_BoardId(int boardId);
    Optional<LikeEntity> findByBoardIdAndNickName(BoardEntity boardId, String nickName);
    List<LikeEntity> findByNickName(String nickName);
}
