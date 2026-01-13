package com.fmp.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 매칭DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MatchDTO {

    private Long matchId;       
    private String user1Id;        
    private String user2Id;            
    private Integer schoolYear;   
    private String major;          
    private String status;        
    private LocalDateTime createdAt; 
}
