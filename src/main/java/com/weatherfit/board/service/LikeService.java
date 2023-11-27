package com.weatherfit.board.service;

import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.domain.LikeEntity;
import com.weatherfit.board.repository.BoardRepository;
import com.weatherfit.board.repository.LikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void like(int boardId, String nickName) {
        BoardEntity boardEntity = boardRepository.findById(boardId);
        Optional<LikeEntity> existingLikeEntity = likeRepository.findByBoardIdAndNickName(boardEntity, nickName);

        if (existingLikeEntity.isPresent()) {
            likeRepository.delete(existingLikeEntity.get());
        } else {
            LikeEntity likeEntity = LikeEntity.builder()
                    .boardId(boardEntity)
                    .nickName(nickName)
                    .build();
            likeRepository.save(likeEntity);
        }
        int likeCount = likeRepository.countByBoardId_BoardId(boardId);
        BoardEntity updatedBoardEntity = boardEntity.toBuilder()
                .likeCount(likeCount)
                .build();
        boardRepository.save(updatedBoardEntity);
    }

}
