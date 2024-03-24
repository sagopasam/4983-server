package team.dankookie.server4983.chat.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.chat.dto.AdminChatMessageResponse;
import team.dankookie.server4983.chat.dto.AdminChatRoomListResponse;
import team.dankookie.server4983.chat.service.AdminChatService;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/admin/chat"))
public class AdminChatController {

    private final AdminChatService adminChatService;

    @GetMapping
    public Page<AdminChatRoomListResponse> getChatList(
            Pageable pageable, @RequestParam(value = "") String searchKeyword,
            @RequestParam int interact) {
        return adminChatService.getChatList(pageable, searchKeyword, interact);

    }

    @PatchMapping("/interact")
    public ResponseEntity<Void> updateInteract(
            @RequestParam Long chatRoomId,
            @RequestParam int interact) {

        adminChatService.updateInteract(chatRoomId, interact);
        return ResponseEntity.ok().build();
    }

  @PutMapping("/{chatRoomId}/cancel")
  public ResponseEntity<Void> cancel(@RequestParam Long chatRoomId) {
    adminChatService.cancel(chatRoomId);
    return ResponseEntity.ok().build();
  }

    @GetMapping("{chatRoomId}/buyer")
    public ResponseEntity<List<AdminChatMessageResponse>> getBuyerChat(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(adminChatService.getBuyerChat(chatRoomId));
    }

    @GetMapping("{chatRoomId}/seller")
    public ResponseEntity<List<AdminChatMessageResponse>> getSellerChat(
            @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(adminChatService.getSellerChat(chatRoomId));
    }

}
