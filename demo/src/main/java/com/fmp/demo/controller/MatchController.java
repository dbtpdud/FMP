package com.fmp.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import com.fmp.demo.dto.MatchDTO;
import com.fmp.demo.service.MatchService;

/*
 * 매칭 controller
 */
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    /**
     * 1) 매칭 생성(대기/붙기)
     * - 정상: MatchDTO 반환 (WAITING 또는 MATCHED)
     * - 로그인 필요: 401
     * - 입력/상태 문제: 400
     * - 서버 오류: 500
     */
    @PostMapping("/create")
    public ResponseEntity<?> createMatch(
            @RequestParam("schoolYear") int schoolYear,
            @RequestParam(value = "major", required = false) String major,
            @RequestParam(value = "sameAll", required = false, defaultValue = "0") int sameAll,
            HttpSession session
    ) {
        String studentId = getStudentIdFromSession(session);
        if (studentId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "LOGIN_REQUIRED"));
        }

        try {
            // ✅ sameAll=1이면 학과까지 동일 / sameAll=0이면 학년만 동일
            MatchDTO match = matchService.createOrJoinMatch(studentId, schoolYear, major, sameAll == 1);

            if (match == null) {
                return ResponseEntity.ok(Map.of("status", "NO_MATCH"));
            }
            return ResponseEntity.ok(match);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    @PostMapping("/create/me")
    public ResponseEntity<?> createMatchByMe(HttpSession session) {
        String studentId = getStudentIdFromSession(session);
        if (studentId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "LOGIN_REQUIRED"));
        }

        try {
            MatchDTO match = matchService.createOrJoinMatchByProfile(studentId);

            if (match == null) {
                return ResponseEntity.ok(Map.of("status", "NO_MATCH"));
            }

            return ResponseEntity.ok(match);

        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }
    
    /**
     * 3) 내 매칭 전체 조회 (세션 기반)
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyMatches(HttpSession session) {
        String studentId = getStudentIdFromSession(session);
        if (studentId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "LOGIN_REQUIRED"));
        }

        try {
            List<MatchDTO> matches = matchService.getMatchesByUserId(studentId);
            return ResponseEntity.ok(matches);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    /**
     * 4) 상태 변경(관리/테스트 용)
     */
    @PutMapping("/{matchId}/status")
    public ResponseEntity<?> updateMatchStatus(
            @PathVariable("matchId") Long matchId,
            @RequestParam("status") String status
    ) {
        try {
            int updated = matchService.updateMatchStatus(matchId, status);
            if (updated > 0) return ResponseEntity.noContent().build();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    /**
     * 5) 매칭 삭제(종료)
     */
    @DeleteMapping("/{matchId}")
    public ResponseEntity<?> deleteMatch(@PathVariable("matchId") Long matchId) {
        try {
            int deleted = matchService.deleteMatch(matchId);
            if (deleted > 0) return ResponseEntity.noContent().build();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    /**
     * 6) 상태 조회(폴링용)
     */
    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@RequestParam("matchId") Long matchId) {
        try {
            MatchDTO match = matchService.getMatchById(matchId);
            if (match == null) {
                return ResponseEntity.status(404).body(Map.of("error", "MATCH_NOT_FOUND"));
            }
            return ResponseEntity.ok(match);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    /**
     * ✅ 채팅방 나가기(종료)
     * - 반드시 "로그인한 본인"이 그 match의 user1 또는 user2일 때만 종료 가능
     */
    @PutMapping("/{matchId}/leave")
    public ResponseEntity<?> leaveMatch(
            @PathVariable("matchId") Long matchId,
            HttpSession session
    ) {
        String studentId = getStudentIdFromSession(session);
        if (studentId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "LOGIN_REQUIRED"));
        }

        try {
            MatchDTO match = matchService.getMatchById(matchId);
            if (match == null) {
                return ResponseEntity.status(404).body(Map.of("error", "MATCH_NOT_FOUND"));
            }

            boolean participant =
                    studentId.equals(match.getUser1Id()) ||
                    (match.getUser2Id() != null && studentId.equals(match.getUser2Id()));

            if (!participant) {
                return ResponseEntity.status(403).body(Map.of("error", "NOT_PARTICIPANT"));
            }

            int updated = matchService.updateMatchStatus(matchId, "ENDED");
            if (updated > 0) return ResponseEntity.ok(Map.of("status", "ENDED"));
            return ResponseEntity.status(409).body(Map.of("error", "UPDATE_FAILED"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "SERVER_ERROR"));
        }
    }

    /**
     * ✅ 세션에서 학번(studentId)을 최대한 안전하게 찾는 함수
     */
    private String getStudentIdFromSession(HttpSession session) {
        if (session == null) return null;

        Object v;

        v = session.getAttribute("studentId");
        if (v instanceof String s && !s.isBlank()) return s;

        v = session.getAttribute("student_id");
        if (v instanceof String s && !s.isBlank()) return s;

        v = session.getAttribute("userId");
        if (v instanceof String s && !s.isBlank()) return s;

        return null;
    }
}
