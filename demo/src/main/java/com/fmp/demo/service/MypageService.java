package com.fmp.demo.service;

import com.fmp.demo.repository.interfaces.MypageRepository;
import com.fmp.demo.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    private final MypageRepository mypageRepository;

    @Autowired
    public MypageService(MypageRepository mypageRepository) {
        this.mypageRepository = mypageRepository;
    }

    // 1. 유저 정보 조회
    public User getUserInfo(String studentId) {
        return mypageRepository.findByStudentId(studentId);
    }

    // 2. 유저 정보 수정
    public void updateUserInfo(User user) {
        int result = mypageRepository.updateUserInfo(user);
        if (result == 0) {
            throw new IllegalStateException("사용자 정보 수정 실패: 존재하지 않거나 이미 탈퇴된 사용자입니다.");
        }
    }

    // 3. 회원 탈퇴
    public void withdrawUser(String studentId) {
        int result = mypageRepository.withdrawUser(studentId);
        if (result == 0) {
            throw new IllegalStateException("회원 탈퇴 실패: 존재하지 않거나 이미 탈퇴된 사용자입니다.");
        }
    }
}
