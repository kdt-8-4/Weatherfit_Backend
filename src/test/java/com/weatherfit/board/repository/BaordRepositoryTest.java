//package com.weatherfit.board.repository;
//
//import com.weatherfit.board.domain.BoardEntity;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//public class BaordRepositoryTest {
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Test
//    void insertTest() {
//
//        for(int i=0; i<100; i++) {
//            BoardEntity boardEntity = BoardEntity.builder()
//                    .likeCount(i)
//                    .content("ㄹㅈ")
//                    .temperature(i)
//                    .nickName("qwe")
//                    .category()
//                    .status(true)
//                    .build();
//            boardRepository.save(boardEntity);
//        }
//
//
//
//    }
//}
