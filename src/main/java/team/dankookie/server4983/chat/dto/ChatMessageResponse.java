package team.dankookie.server4983.chat.dto;

import team.dankookie.server4983.chat.constant.ContentType;

import java.time.LocalDateTime;

public record ChatMessageResponse (
        String message,
        ContentType contentType,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse of(
            String message,
            ContentType contentType,
            LocalDateTime createdAt
    ) {
        return new ChatMessageResponse(
                message,
                contentType,
                createdAt
        );
    }
}
