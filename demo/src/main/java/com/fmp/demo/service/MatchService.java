package com.fmp.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fmp.demo.dto.MatchDTO;
import com.fmp.demo.repository.interfaces.MatchRepository;

import com.fmp.demo.repository.interfaces.UserRepository;
import com.fmp.demo.dto.MatchProfile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;
    
    /**
     * ✅ 최종 안정 버전
     * 1) 항상 먼저 "남의 WAITING"에 붙기 시도
     * 2) 없으면 "내 WAITING"이 이미 있는지 확인해서 있으면 재사용
     * 3) 그것도 없으면 새 WAITING 생성
     */
    @Transactional
    public MatchDTO createOrJoinMatch(String myUserId, int schoolYear, String major) {

        if (myUserId == null || myUserId.isBlank()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        String normMajor = (major == null) ? null : major.trim();
        if (normMajor == null || normMajor.isBlank()) {
            throw new IllegalStateException("전공 값이 올바르지 않습니다.");
        }

        // 1) ✅ 항상 먼저 "남의 WAITING"을 찾아서 붙기
        MatchDTO waiting = matchRepository.findWaitingMatch(schoolYear, normMajor, myUserId);
        if (waiting != null) {
            int updated = matchRepository.joinWaitingMatch(waiting.getMatchId(), myUserId);
            if (updated == 1) {
                return matchRepository.getMatchById(waiting.getMatchId());
            }
            // updated가 0이면: 누가 먼저 붙어서 바뀐 것 → 아래 흐름으로 계속 진행
        }

        // 2) ✅ 남의 WAITING이 없으면, 내가 이미 WAITING인지 확인해서 재사용
        MatchDTO myWaiting = matchRepository.findMyWaitingMatch(myUserId, schoolYear, normMajor);
        if (myWaiting != null) {
            return myWaiting;
        }

        // 3) ✅ 없으면 내 WAITING 새로 생성
        MatchDTO newWaiting = MatchDTO.builder()
                .user1Id(myUserId)
                .user2Id(null)
                .schoolYear(schoolYear)
                .major(normMajor)
                .status("WAITING")
                .build();

        matchRepository.createWaitingMatch(newWaiting);

        // 4) ✅ 레이스 대응: 생성 직후 다시 한번 남의 WAITING 붙기 시도 (타이밍 꼬임 방지)
        MatchDTO waiting2 = matchRepository.findWaitingMatch(schoolYear, normMajor, myUserId);
        if (waiting2 != null) {
            int updated2 = matchRepository.joinWaitingMatch(waiting2.getMatchId(), myUserId);
            if (updated2 == 1) {
                // 내가 만든 WAITING은 필요 없어졌으니 정리
                matchRepository.deleteMatch(newWaiting.getMatchId());
                return matchRepository.getMatchById(waiting2.getMatchId());
            }
        }

        return matchRepository.getMatchById(newWaiting.getMatchId());
    }
    
    
    @Transactional
    public MatchDTO createOrJoinMatch(String myUserId, int schoolYear, String major, boolean sameAll) {

        if (myUserId == null || myUserId.isBlank()) {
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        if (schoolYear < 1 || schoolYear > 4) {
            throw new IllegalStateException("학년 값이 올바르지 않습니다.");
        }

        String normMajor = (major == null) ? null : major.trim();

        // ✅ sameAll=true면 major 필수, false면 major 무시(학년만 매칭)
        if (sameAll) {
            if (normMajor == null || normMajor.isBlank()) {
                throw new IllegalStateException("전공 값이 올바르지 않습니다.");
            }
        } else {
            normMajor = null; // 학년만 매칭할 거라 major 조건 제거
        }

        // 1) 남의 WAITING 찾기 (sameAll 여부에 따라 쿼리 다름)
        MatchDTO waiting = sameAll
                ? matchRepository.findWaitingMatchSameAll(schoolYear, normMajor, myUserId)
                : matchRepository.findWaitingMatchSchoolOnly(schoolYear, myUserId);

        if (waiting != null) {
            int updated = matchRepository.joinWaitingMatch(waiting.getMatchId(), myUserId);
            if (updated == 1) {
                return matchRepository.getMatchById(waiting.getMatchId());
            }
        }

        // 2) 내가 이미 WAITING인지 확인
        MatchDTO myWaiting = sameAll
                ? matchRepository.findMyWaitingMatchSameAll(myUserId, schoolYear, normMajor)
                : matchRepository.findMyWaitingMatchSchoolOnly(myUserId, schoolYear);

        if (myWaiting != null) return myWaiting;

        // 3) WAITING 생성
        MatchDTO newWaiting = MatchDTO.builder()
                .user1Id(myUserId)
                .user2Id(null)
                .schoolYear(schoolYear)
                .major(normMajor) // sameAll=false면 null 들어감
                .status("WAITING")
                .build();

        matchRepository.createWaitingMatch(newWaiting);

        // 4) 레이스 대응 1번 더 시도
        MatchDTO waiting2 = sameAll
                ? matchRepository.findWaitingMatchSameAll(schoolYear, normMajor, myUserId)
                : matchRepository.findWaitingMatchSchoolOnly(schoolYear, myUserId);

        if (waiting2 != null) {
            int updated2 = matchRepository.joinWaitingMatch(waiting2.getMatchId(), myUserId);
            if (updated2 == 1) {
                matchRepository.deleteMatch(newWaiting.getMatchId());
                return matchRepository.getMatchById(waiting2.getMatchId());
            }
        }

        return matchRepository.getMatchById(newWaiting.getMatchId());
    }

   
    public MatchDTO createOrJoinMatchByProfile(String studentId) {

        // 1) DB에서 내 프로필(학년/전공) 가져오기
        MatchProfile profile = userRepository.findMatchProfileByStudentId(studentId);
        if (profile == null) {
            throw new IllegalStateException("USER_PROFILE_NOT_FOUND");
        }

        // 2) 기존 로직 재사용
        return createOrJoinMatch(studentId, profile.getSchoolYear(), profile.getMajor());
    }

    /** 단건 조회 */
    public MatchDTO getMatchById(Long matchId) {
        return matchRepository.getMatchById(matchId);
    }

    /** 내 매칭 전체 조회 */
    public List<MatchDTO> getMatchesByUserId(String userId) {
        return matchRepository.getMatchesByUserId(userId);
    }

    /** 상태 변경 */
    public int updateMatchStatus(Long matchId, String status) {
        return matchRepository.updateMatchStatus(matchId, status);
    }

    /** 삭제 */
    public int deleteMatch(Long matchId) {
        return matchRepository.deleteMatch(matchId);
    }

    /** 상태 조회용 */
    public MatchDTO getMatchOrThrow(Long matchId) {
        MatchDTO match = matchRepository.getMatchById(matchId);
        if (match == null) throw new IllegalStateException("매칭 정보를 찾을 수 없습니다.");
        return match;
    }
}
