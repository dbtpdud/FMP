package com.fmp.demo.repository.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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
public class User {
	
	private String studentId;
	private String department;
	private Integer schoolYear;
	private String major;
	private String gender;
	private String name;
	private String phone;
	private String userId;
	private String password;
	private LocalDateTime joinDate;
	private boolean deleted;
	private boolean verified;
	
	@Lob
	@Column(name = "profile_image")
	// 프로필 이미지 
	// TODO 추후 수정
	private byte[] profileImage;
	
    @PrePersist
    public void prePersist() {
        this.joinDate = LocalDateTime.now();
        this.deleted = false;
        this.verified = false;
    }
	

}
