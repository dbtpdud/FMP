package com.fmp.demo.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import com.fmp.demo.repository.model.User;

@Mapper
public interface UserRepository {

	/**
	 * 로그인
	 */
	// 사용자 존재 확인 및 중복 확인용
	public int findAllByUser(User user);
	//로그인 기능
	public User findByUserIdAndPassword(@Param("userId") String userId, @Param("password") String password);
	
	//회원가입용 메서드 추가!
	//아이디 비밀번호 찾기
	@Select("SELECT user_id FROM users WHERE name = #{name} AND phone = #{phone}")
	String findUserIdByNameAndPhone(@Param("name") String name, @Param("phone") String phone);

	@Select("SELECT password FROM users WHERE user_id = #{userId} AND name = #{name} AND phone = #{phone}")
	String findPassword(@Param("userId") String userId, @Param("name") String name, @Param("phone") String phone);

	
	/**
	 * 회원가입
	 */
	// user 생성
	public int insertUser(User user);
	
}
