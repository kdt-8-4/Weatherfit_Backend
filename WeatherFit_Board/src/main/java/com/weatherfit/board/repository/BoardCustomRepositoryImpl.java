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

public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<BoardEntity> findBoardEntitiesWithCategoriesAndHashtags(List<String> categories, List<String> hashtags) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BoardEntity> cq = cb.createQuery(BoardEntity.class);

        Root<BoardEntity> board = cq.from(BoardEntity.class);
        List<Predicate> predicates = new ArrayList<>();

        if (categories != null && !categories.isEmpty()) {
            List<Predicate> categoryPredicates = new ArrayList<>();
            for (String category : categories) {
                categoryPredicates.add(cb.isMember(category, board.get("category")));
            }
            predicates.add(cb.and(categoryPredicates.toArray(new Predicate[0])));
        }

        if (hashtags != null && !hashtags.isEmpty()) {
            List<Predicate> hashtagPredicates = new ArrayList<>();
            for (String hashtag : hashtags) {
                hashtagPredicates.add(cb.isMember(hashtag, board.get("hashTag")));
            }
            predicates.add(cb.and(hashtagPredicates.toArray(new Predicate[0])));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(cq).getResultList();
    }


}
