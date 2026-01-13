package com.fmp.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 로그인 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignInDTO {
	
	private String userId; // 아이디
	private String password; // 비밀번호

//	public User toUser() {
//		return User.builder()
//				.userId(this.userId)
//				.password(this.password)
//				.build();
//	}
	
}
