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
public class Friends {
	
	private String userId;
	private String friendId;
	private String status;
	private LocalDateTime createdAt;

}
