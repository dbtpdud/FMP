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
public class Match {

	private Long matchId; 
	private String user1Id; // 사용자1 ID
	private String user2Id; // 사용자2 ID
	private String department; // 매칭된 학부
	private Integer schoolYear; // 매칭된 학년
	private String major; // 매칭된 학과
	private String status; // 매칭 상태 (WAITING 등)
	private LocalDateTime createdAt;
	
	

}
