package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import java.util.List;

public interface BoardCustomRepository {
    List<BoardEntity> findBoardEntitiesWithCategoriesAndHashtags(List<String> categories, List<String> hashtags);


}
