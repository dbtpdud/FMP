package com.fmp.demo.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fmp.demo.repository.model.Chats;

@Mapper
public interface ChatsRepository {

    // 메시지 저장
    void insertChatMessage(Chats chat);

    // 두 유저의 전체 대화 내역 조회
    List<Chats> selectChatMessages(
        @Param("userId1") String userId1,
        @Param("userId2") String userId2
    );

    // 두 유저의 전체 대화 내역 삭제
    void deleteChatMessages(
        @Param("userId1") String userId1,
        @Param("userId2") String userId2
    );
	
}
