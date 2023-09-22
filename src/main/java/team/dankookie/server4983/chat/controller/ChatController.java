package team.dankookie.server4983.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.dto.ChatRoomResponse;
import team.dankookie.server4983.chat.service.ChatService;
import team.dankookie.server4983.jwt.dto.AccessToken;

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

    @GetMapping("/chat/{chatRoomId}")
    public ResponseEntity<List<ChatMessageResponse>> getChattingData(@PathVariable long chatRoomId, AccessToken accessToken) {
        List<ChatMessageResponse> chattingMessageList = chatService.getChattingData(chatRoomId, accessToken);

        return ResponseEntity.ok().body(chattingMessageList);
    }

    @PostMapping("/chat-room")
    public ResponseEntity createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        ChatRoomResponse result = chatService.createChatRoom(chatRoomRequest , request);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/chat-room/{chatRoom}")
    public ResponseEntity getChatRoom(@PathVariable long chatRoom , HttpServletRequest request) {
        ChatRoomResponse result = chatService.getChatRoom(chatRoom , request);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/chat/not-read/{chatRoomId}/{type}")
    public ResponseEntity getNotReadChattingData(@PathVariable long chatRoomId , @PathVariable String type) {
        Object result = chatService.getNotReadChattingData(chatRoomId , type);

        return ResponseEntity.ok().body(result);
    }

}
