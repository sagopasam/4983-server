package team.dankookie.server4983.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.service.ChatService;

import javax.security.auth.login.AccountException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity requestChatBot(@RequestBody ChatRequest chatRequest , HttpServletRequest request) {
        chatService.chatRequestHandler(chatRequest , request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/chat/{chatRoomId}/seller")
    public ResponseEntity getSellerChatting(@PathVariable long chatRoomId) {
        List<SellerChat> result = chatService.getSellerChatting(chatRoomId);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/chat/{chatRoomId}/buyer")
    public ResponseEntity getBuyerChatting(@PathVariable long chatRoomId) {
        List<BuyerChat> result = chatService.getBuyerChatting(chatRoomId);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/chat-room")
    public ResponseEntity createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        Long result = chatService.createChatRoom(chatRoomRequest , request);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

}
