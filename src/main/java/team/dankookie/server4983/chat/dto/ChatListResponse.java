package team.dankookie.server4983.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record ChatListResponse(
    long chatRoomId,
    String usedBookName,
    String message,
    LocalDateTime createdAt,
    boolean isRead,
    String imageUrl,
    long usedBookId
) {
    public static ChatListResponse of(
        long chatRoomId,
        String usedBookName,
        String message,
        LocalDateTime createdAt,
        boolean isRead,
        String imageUrl,
        long usedBookId
    ) {
        return new ChatListResponse(
            chatRoomId,
            usedBookName,
            message,
            createdAt,
            isRead,
            imageUrl,
            usedBookId
        );
    }

    @QueryProjection
    public ChatListResponse(long chatRoomId, String usedBookName, String message,
        LocalDateTime createdAt, boolean isRead, String imageUrl, long usedBookId) {
        this.chatRoomId = chatRoomId;
        this.usedBookName = usedBookName;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
        this.imageUrl = imageUrl;
        this.usedBookId = usedBookId;
    }
}
