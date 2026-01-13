package com.fmp.demo.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * MatchingService: 랜덤 매칭을 구현하면서
 *                  매칭된 두 사용자에게 모두 같은 roomId를 반환하도록 개선된 버전
 */
@Service
@Slf4j
public class MatchingService {

    /** 대기 중인 사용자 ID를 저장하는 큐 */
    private final ConcurrentLinkedQueue<String> waitingQueue = new ConcurrentLinkedQueue<>();

    /** 매칭이 성사된 사용자(userId)별로 방 ID를 저장하는 맵 */
    private final Map<String, String> matchedRooms = new ConcurrentHashMap<>();

    /**
     * userId를 대기열에 등록하고, 만약 대기열에 이미 다른 사용자가 있으면 바로 매칭을 성사시킨다.
     * 매칭된 두 사용자 모두에게 같은 roomId를 반환해야 한다.
     *
     * @param userId 채팅에 참여하려는 사용자(세션ID 등)
     * @return roomId (매칭이 성사되지 않으면 null, 성사된 순간에는 두 사용자 모두 동일한 roomId를 반환)
     */
    public synchronized String joinQueueAndMatch(String userId) {
        // 1) 이미 매칭된 적 있다면, 그 roomId를 돌려주고 matchedRooms에서 제거
        if (matchedRooms.containsKey(userId)) {
            String roomId = matchedRooms.remove(userId);
            log.info("User({}) found matched room: {}", userId, roomId);
            return roomId;
        }

        // 2) 대기열에 이미 들어가 있는 상태라면(중복 방지)
        if (waitingQueue.contains(userId)) {
            // 아직 매칭되지 않았으니까 null 반환
            return null;
        }

        // 3) 현재 대기열이 비어 있다면, userId를 큐에 넣고 null 리턴 (아직 상대가 없음)
        if (waitingQueue.isEmpty()) {
            waitingQueue.offer(userId);
            log.info("User({}) added to waiting queue. Waiting for opponent...", userId);
            return null;
        }

        // 4) 대기열에 이미 누군가가 기다리고 있는 상태이면, 즉시 매칭
        String opponentId = waitingQueue.poll(); // 대기 큐에서 먼저 들어온 사용자 하나 꺼냄
        // 두 명 매칭 → 새로운 roomId 생성
        String roomId = generateRoomId();
        log.info("Matched users: {} <-> {} in room: {}", opponentId, userId, roomId);

        // 5) 매칭된 두 명 모두에게 같은 roomId를 저장
        matchedRooms.put(opponentId, roomId);
        matchedRooms.put(userId, roomId);

        // 6) 이 메서드를 호출한 측(= userId)에게 즉시 방 ID를 돌려주면,
        //    나중에 opponentId도 같은 roomId를 가져갈 수 있도록 위에서 저장해 뒀다.
        return roomId;
    }

    /** UUID를 활용해 고유한 room ID 생성 */
    private String generateRoomId() {
        return "room-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
