package com.fmp.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fmp.demo.dto.MatchDTO;
import com.fmp.demo.service.MatchService;

import lombok.RequiredArgsConstructor;

/*
 * 매칭 controller
 */

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

 // 1. 매칭 생성 (랜덤)
    @PostMapping("/create")
    public ResponseEntity<?> createMatch(
            @RequestParam("schoolYear") int schoolYear,
            @RequestParam("major") String major
    ) {
        try {
            MatchDTO match = matchService.createRandomMatch(schoolYear, major);
            return ResponseEntity.ok(match);
        } catch (IllegalStateException e) {
            // 후보 부족 등 예외 상황도 클라이언트에서 쉽게 알 수 있게!
            return ResponseEntity
                    .badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // 2. 단건 조회
    @GetMapping("/{matchId}")
    public ResponseEntity<MatchDTO> getMatchById(@PathVariable("matchId") Long matchId) {
        MatchDTO match = matchService.getMatchById(matchId);
        if (match == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(match);
    }

    // 3. 내 매칭 전체 조회
    @GetMapping("/my")
    public ResponseEntity<List<MatchDTO>> getMatchesByUserId(@RequestParam("userId") String userId) {
        List<MatchDTO> matches = matchService.getMatchesByUserId(userId);
        return ResponseEntity.ok(matches);
    }

 // 4. 상태 변경
    @PutMapping("/{matchId}/status")
    public ResponseEntity<Void> updateMatchStatus(
            @PathVariable("matchId") Long matchId,
            @RequestParam("status") String status) {
        int updated = matchService.updateMatchStatus(matchId, status);
        if (updated > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. 매칭 삭제(종료)
    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("matchId") Long matchId) {
        int deleted = matchService.deleteMatch(matchId);
        if (deleted > 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

