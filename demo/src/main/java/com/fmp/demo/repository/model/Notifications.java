package com.fmp.demo.repository.model;

import java.time.LocalDateTime;

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
public class Notifications {
	
	private Long notificationId;
	private String recipientId;
	private String text;
	private boolean isRead;
	private LocalDateTime timestamp;

}
