package com.fmp.demo.repository.interfaces;

import com.fmp.demo.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageRepository {
    User findByStudentId(String studentId);
    int updateUserInfo(User user);
    int withdrawUser(String studentId);
}
