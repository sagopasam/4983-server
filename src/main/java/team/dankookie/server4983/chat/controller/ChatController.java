package team.dankookie.server4983.chat.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.service.ChatService;

import javax.security.auth.login.AccountException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity requestChatBot(@RequestBody ChatRequest chatRequest) {
        String result = chatService.chatRequestHandler(chatRequest);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/chat-room")
    public ResponseEntity createChatRoom(@RequestBody ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        chatService.createChatRoom(chatRoomRequest , request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
