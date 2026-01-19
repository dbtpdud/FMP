package com.fmp.demo.controller;

import java.time.Instant;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.fmp.demo.dto.ChatsDTO;

@Controller
public class ChatController {

	// (1) 기존: 채팅 메시지 브로드캐스트
	@MessageMapping("/chat/{roomId}")
	@SendTo("/topic/chat/{roomId}")
	public ChatsDTO sendRoomMessage(@DestinationVariable("roomId") String roomId, ChatsDTO chatsDTO) {


		return chatsDTO;
	}

	// (2) 새로 추가: 나가기 요청 처리
	@MessageMapping("/chat/{roomId}/leave")
	@SendTo("/topic/chat/{roomId}")
	public ChatsDTO leaveRoom(

		@DestinationVariable
		("roomId") String roomId, ChatsDTO chatsDTO) {
		String nowIso = Instant.now().toString();
		ChatsDTO leaveNotice = new ChatsDTO();
		leaveNotice.setSenderId(chatsDTO.getSenderId());
		leaveNotice.setText("LEAVE");
		leaveNotice.setTimestamp1(nowIso);
		return leaveNotice;
	}
}
