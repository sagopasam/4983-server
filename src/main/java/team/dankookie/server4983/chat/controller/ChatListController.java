package team.dankookie.server4983.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.service.ChatService;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ChatListController {

    private final ChatService chatService;

    @GetMapping("/chat/list")
    public List<ChatListResponse> getChatList(AccessToken accessToken) {
        return chatService.getChatListWithAccessToken(accessToken);
    }
}
