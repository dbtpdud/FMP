package com.fmp.demo.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fmp.demo.dto.MatchDTO;
import com.fmp.demo.repository.interfaces.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {
	
	 private final MatchRepository matchRepository;

	// 랜덤 매칭을 여러 번 반복할 수 있도록 구현!
	    public MatchDTO createRandomMatch(int schoolYear, String major) {
	        List<String> candidates = matchRepository.findAvailableUserIds(schoolYear, major);

	        // 후보가 2명 이상이면 OK
	        if (candidates.size() < 2) {
	            throw new IllegalStateException("매칭 가능한 사용자가 부족합니다.");
	        }

	        // 매번 호출할 때마다 무작위로 두 명 뽑기!
	        Collections.shuffle(candidates);
	        String user1 = candidates.get(0);
	        String user2 = candidates.get(1);

	        MatchDTO matchDTO = MatchDTO.builder()
	                .user1Id(user1)
	                .user2Id(user2)
	                .schoolYear(schoolYear)
	                .major(major)
	                .status("WAITING")
	                .build();

	        matchRepository.createMatch(matchDTO);
	        return matchDTO;
	    }

	    // 단건 조회
	    public MatchDTO getMatchById(Long matchId) {
	        return matchRepository.getMatchById(matchId);
	    }

	    // 내 매칭 전체 조회
	    public List<MatchDTO> getMatchesByUserId(String userId) {
	        return matchRepository.getMatchesByUserId(userId);
	    }

	    // 상태 변경
	    public int updateMatchStatus(Long matchId, String status) {
	        return matchRepository.updateMatchStatus(matchId, status);
	    }

	    // 매칭 삭제(종료)
	    public int deleteMatch(Long matchId) {
	        return matchRepository.deleteMatch(matchId);
	    }

}
