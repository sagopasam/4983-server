package team.dankookie.server4983.chat.dto;

import java.time.LocalDateTime;

public record ChatListResponse(
        String imageUrl,
        String usedBookName,
        String message,
        LocalDateTime createdAt,
        boolean isRead
) {

    public static ChatListResponse of(String imageUrl, String usedBookName, String message, LocalDateTime createdAt, boolean isRead) {
        return new ChatListResponse(imageUrl, usedBookName, message, createdAt, isRead);
    }
}
