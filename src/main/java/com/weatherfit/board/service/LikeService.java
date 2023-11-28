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
    public void like(int boardId, String decodedNickname) {
        BoardEntity boardEntity = boardRepository.findById(boardId);
        Optional<LikeEntity> existingLikeEntity = likeRepository.findByBoardIdAndNickName(boardEntity, decodedNickname);

        if (existingLikeEntity.isPresent()) {
            likeRepository.delete(existingLikeEntity.get());
        } else {
            LikeEntity likeEntity = LikeEntity.builder()
                    .boardId(boardEntity)
                    .nickName(decodedNickname)
                    .build();
            likeRepository.save(likeEntity);
        }
    }
    public int countLikes(int boardId) {
        return likeRepository.countByBoardId_BoardId(boardId);
    }

}
