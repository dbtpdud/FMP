package com.fmp.demo.dto;

import java.time.LocalDateTime;

import com.fmp.demo.repository.model.User;

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
public class UserDTO {
	
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

	
	public UserDTO(User user) {
		this.studentId = user.getStudentId();
		this.department = user.getDepartment();
		this.schoolYear = user.getSchoolYear();
		this.major = user.getMajor();
		this.gender = user.getGender();
		this.name = user.getName();
		this.phone = user.getPhone();
		this.userId = user.getUserId();
		this.password = user.getPassword();
		this.joinDate = user.getJoinDate();
		
	}

}
