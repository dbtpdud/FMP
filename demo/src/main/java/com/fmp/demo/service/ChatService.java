package com.fmp.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fmp.demo.repository.interfaces.ChatsRepository;
import com.fmp.demo.repository.model.Chats;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {
	
	   private final ChatsRepository chatsRepository;

	    // 메시지 저장
	    public void saveChatMessage(Chats chat) {
	        chatsRepository.insertChatMessage(chat);
	    }

	    // 대화 조회
	    public List<Chats> getChatMessages(String userId1, String userId2) {
	        return chatsRepository.selectChatMessages(userId1, userId2);
	    }

	    // 대화 삭제
	    public void deleteChatMessages(String userId1, String userId2) {
	        chatsRepository.deleteChatMessages(userId1, userId2);
	    }

}
