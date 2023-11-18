package com.weatherfit.board.repository;

import com.weatherfit.board.domain.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
}
