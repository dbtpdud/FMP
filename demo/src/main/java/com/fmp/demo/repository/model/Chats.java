package com.fmp.demo.repository.model;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Chats {

	// TODO 필요시 수정
	private Long chatId;
	private String senderId;
	private String receiverId;
	private String message;
	private LocalDateTime timestamp;

	@PrePersist
	private void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
}
