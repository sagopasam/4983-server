package team.dankookie.server4983.chat.controller;

import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PURCHASE_START;

import java.util.List;
import javax.security.auth.login.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponseWithUsedBookId;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.dto.ChatRoomResponse;
import team.dankookie.server4983.chat.dto.ChatStopRequest;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.service.ChatService;
import team.dankookie.server4983.jwt.dto.AccessToken;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-room")
public class ChatController {

  private final ChatService chatService;
  private final ChatLogicHandler chatLogicHandler;
  @PostMapping
  public ResponseEntity<ChatRoomResponse> createChatRoom(
      @RequestBody ChatRoomRequest chatRoomRequest,
      AccessToken accessToken
  ) throws AccountException {
    ChatRoomResponse result = chatService.createChatRoom(chatRoomRequest, accessToken);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping("/{chatRoomId}")
  public ResponseEntity<List<ChatMessageResponse>> getChattingData(@PathVariable long chatRoomId,
      AccessToken accessToken) {
    List<ChatMessageResponse> chattingMessageList = chatService.getChattingData(chatRoomId,
        accessToken);

    return ResponseEntity.ok().body(chattingMessageList);
  }

  @PostMapping("/interact")
  public ResponseEntity<List<ChatMessageResponse>> requestChatBot(
      @RequestBody ChatRequest chatRequest,
      AccessToken accessToken) {

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(chatLogicHandler.chatLogic(chatRequest));
  }

  @GetMapping("/not-read/{chatRoomId}")
  public ResponseEntity<List<ChatMessageResponse>> getNotReadChattingData(
      @PathVariable Long chatRoomId,
      AccessToken accessToken) {
    List<ChatMessageResponse> responseList = chatService.getNotReadChattingData(chatRoomId,
        accessToken);

    return ResponseEntity.ok().body(responseList);
  }

  @PostMapping("/stop")
  public ResponseEntity<List<ChatMessageResponseWithUsedBookId>> stopTrade(
      @RequestBody ChatStopRequest chatStopRequest, AccessToken accessToken) {

    return ResponseEntity.ok(chatService.stopTrade(chatStopRequest, accessToken));
  }

  @DeleteMapping("/{chatRoomId}")
  public ResponseEntity<Void> deleteUsedBookByChatRoomId(@PathVariable Long chatRoomId,
      AccessToken accessToken) {
    chatService.deleteUsedBookByChatRoomIdWhenSellerCanceledChat(chatRoomId, accessToken);

    return ResponseEntity.noContent().build();
  }
}
