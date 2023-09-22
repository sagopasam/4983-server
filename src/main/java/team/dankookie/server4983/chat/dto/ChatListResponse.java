package team.dankookie.server4983.chat.dto;

import java.time.LocalDateTime;

public record ChatListResponse(
    long chatRoomId,
    String usedBookName,
    String message,
    LocalDateTime createdAt,
    boolean isRead,
    String imageUrl
) {
    public static ChatListResponse of(
        long chatRoomId,
        String usedBookName,
        String message,
        LocalDateTime createdAt,
        boolean isRead,
        String imageUrl
    ) {
        return new ChatListResponse(
            chatRoomId,
            usedBookName,
            message,
            createdAt,
            isRead,
            imageUrl
        );
    }
}
