package com.fmp.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fmp.demo.repository.model.Chats;
import com.fmp.demo.service.ChatService;

import lombok.RequiredArgsConstructor;

/**
 * ChatsController: 기존에 쓰시던 "/dm" 기반의 뷰(View) 및 REST API 컨트롤러
 */
@Controller
@RequestMapping("/dm") // http://localhost:8080/dm
@RequiredArgsConstructor
public class ChatsController {

    private final ChatService chatsService;

    /**
     * 채팅 화면(view)으로 포워딩
     * - 랜덤 매칭을 눌러서 방이 생겼다면, JS에서 roomId를 가진 채팅 페이지로 이동하거나
     *   query param 으로 붙여서 들어오면, JSP/Thymeleaf(또는 static HTML)에서도 roomId를 받아올 수 있습니다.
     */
    @GetMapping({ "", "/" })
    public String chatRoot(Model model) {
        // model.addAttribute("roomId", null);  // 매칭 후 파라미터를 통해 roomId를 넣어 주세요
        return "chat";
    }

    /**
     * 방 ID를 PathVariable로 받아 채팅 화면(view)을 렌더링 
     * 예시 URL: http://localhost:8080/dm/room-12345
     */
    @GetMapping("/{roomId}")
    public String chatWithRoomId(@PathVariable("roomId") String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        return "chat"; // templates/chat.html (Thymeleaf) 또는 static/chat.html (JS 코드가 URL에서 roomId 추출)
    }

    /**
     * (기존) 1:1 채팅 내역 조회 (REST API)
     */
    @GetMapping("/messages")
    @ResponseBody
    public List<Chats> getChatMessages(@RequestParam String userId1, @RequestParam String userId2) {
        return chatsService.getChatMessages(userId1, userId2);
    }

    /**
     * (기존) 채팅 내역 전체 삭제 (REST API)
     */
    @DeleteMapping("/messages")
    @ResponseBody
    public void deleteChatMessages(@RequestParam String userId1, @RequestParam String userId2) {
        chatsService.deleteChatMessages(userId1, userId2);
    }
}
