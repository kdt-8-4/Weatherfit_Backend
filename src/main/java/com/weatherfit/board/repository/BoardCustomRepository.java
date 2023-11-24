package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import com.weatherfit.board.dto.BoardSearchDTO;

import java.util.List;

public interface BoardCustomRepository {
    List<BoardEntity> findBoardEntitiesWithCategoriesAndHashtags(List<String> categories, List<String> hashtags);


}
