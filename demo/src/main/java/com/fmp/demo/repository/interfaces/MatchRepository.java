package com.fmp.demo.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fmp.demo.dto.MatchDTO;

// 매칭 인터페이스
// 생성(저장), 조회, 업데이트(상태변경), 삭제(매칭 종료)
@Mapper
public interface MatchRepository {
	
	  /** 1. 매칭 가능한 사용자 후보 조회 */
    List<String> findAvailableUserIds(
        @Param("schoolYear") int schoolYear,
        @Param("major") String major
    );

    /** 2. 매칭 생성(저장) */
    int createMatch(MatchDTO matchDTO);

    /** 3. 단건 조회 (matchId 기준) */
    MatchDTO getMatchById(@Param("matchId") Long matchId);

    /** 4. 내 매칭 전체 조회 (userId 기준) */
    List<MatchDTO> getMatchesByUserId(@Param("userId") String userId);

    /** 5. 매칭 상태 변경(업데이트) */
    int updateMatchStatus(
        @Param("matchId") Long matchId,
        @Param("status") String status
    );

    /** 6. 매칭 삭제(종료) */
    int deleteMatch(@Param("matchId") Long matchId);

}
