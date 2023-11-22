package com.weatherfit.board.repository;

import com.weatherfit.board.domain.BoardEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    @PersistenceContext
    private EntityManager em;
    @Override
    public List<BoardEntity> findBoardEntitiesWithCategoriesAndHashtags(List<String> categories, List<String> hashtags) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> cq = cb.createQuery(BoardEntity.class);
        Root<BoardEntity> board = cq.from(BoardEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        for(String category : categories) {
            predicates.add(cb.isMember(category, board.get("category")));
        }
        for(String hashtag : hashtags) {
            predicates.add(cb.isMember(hashtag, board.get("hashtag")));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(cq).getResultList();
    }

}
