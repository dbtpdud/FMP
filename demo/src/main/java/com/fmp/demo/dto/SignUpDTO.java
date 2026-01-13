package com.fmp.demo.dto;

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
public class SignUpDTO {

    private String userId;         // 사용자 아이디 (로그인용)
    private String password;       // 사용자 비밀번호
    private String name;           // 사용자 이름
    private String phone;          // 전화번호 (예: 010-1234-5678)
    private String gender;         // 성별 (남자 / 여자)
    private String department;     // 학부 (예: 공과대학)
    private String major;          // 학과 (예: 컴퓨터공학과)
    private Integer schoolYear;    // 학년 (1~4학년 등)
    private String studentId;      // 학번 (예: 202312345)
    private String profileImage;   // 프로필 이미지 경로 또는 파일명 (선택사항)
}