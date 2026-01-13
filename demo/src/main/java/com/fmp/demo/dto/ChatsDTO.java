package com.fmp.demo.dto;

import java.time.LocalDateTime;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 채팅 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChatsDTO {
	
	private Long chatId;
	private String senderId;
	private String receiverId;
	private String text;
	private LocalDateTime timestamp;
	private String timestamp1;

	@PrePersist
	private void onCreate() {
		this.timestamp = LocalDateTime.now();
	}
	
//	public ChatsDTO() {}
	
	public ChatsDTO(String senderId, String text, String timestamp1) {
		this.senderId = senderId;
		this.text = text;
		this.timestamp1 = timestamp1;
	}
	
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp1() {
        return timestamp1;
    }
    public void setTimestamp1(String timestamp1) {
        this.timestamp1 = timestamp1;
    }
	

}
