package com.fmp.demo.service;

import org.springframework.stereotype.Service;

import com.fmp.demo.dto.UserDTO;
import com.fmp.demo.repository.interfaces.UserRepository;
import com.fmp.demo.repository.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	
private final UserRepository userRepository;

    /**
     * 로그인 처리: 성공 시 UserDTO 반환, 실패 시 null
     */
    public UserDTO login(String userId, String password) {
        User user = userRepository.findByUserIdAndPassword(userId, password);
        if (user == null) return null;
        UserDTO dto = new UserDTO(user);
        dto.setPassword(null); // ← 응답에 비밀번호는 포함하지 않도록!
        return dto;
    }
    
    //회원가입용 메서드
    public int registerUser(User user) {
        return userRepository.insertUser(user);
    }
}
