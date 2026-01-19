package com.fmp.demo.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fmp.demo.dto.MatchDTO;

@Mapper
public interface MatchRepository {

    MatchDTO getMatchById(@Param("matchId") Long matchId);

    List<MatchDTO> getMatchesByUserId(@Param("userId") String userId);

    int updateMatchStatus(@Param("matchId") Long matchId, @Param("status") String status);

    int deleteMatch(@Param("matchId") Long matchId);

    // ✅ (기존) 학년+학과 동일 매칭용
    MatchDTO findWaitingMatch(@Param("schoolYear") int schoolYear,
                              @Param("major") String major,
                              @Param("myUserId") String myUserId);

    MatchDTO findMyWaitingMatch(@Param("myUserId") String myUserId,
                                @Param("schoolYear") int schoolYear,
                                @Param("major") String major);

    // ✅ (최종형) sameAll=1 : 학년+학과 동일
    MatchDTO findWaitingMatchSameAll(@Param("schoolYear") int schoolYear,
                                     @Param("major") String major,
                                     @Param("myUserId") String myUserId);

    MatchDTO findMyWaitingMatchSameAll(@Param("myUserId") String myUserId,
                                       @Param("schoolYear") int schoolYear,
                                       @Param("major") String major);

    // ✅ (최종형) sameAll=0 : 학년만 동일 (major IS NULL)
    MatchDTO findWaitingMatchSchoolOnly(@Param("schoolYear") int schoolYear,
                                        @Param("myUserId") String myUserId);

    MatchDTO findMyWaitingMatchSchoolOnly(@Param("myUserId") String myUserId,
                                          @Param("schoolYear") int schoolYear);

    // ✅ WAITING에 붙기
    int joinWaitingMatch(@Param("matchId") Long matchId,
                         @Param("myUserId") String myUserId);

    // ✅ WAITING 만들기
    int createWaitingMatch(MatchDTO dto);
}
