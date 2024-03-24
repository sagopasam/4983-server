package team.dankookie.server4983.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import team.dankookie.server4983.chat.constant.ContentType;

public record ChatMessageResponseWithUsedBookId(
    long chatRoomId,
    String message,
    ContentType contentType,
    LocalDateTime createdAt,
    long usedBookId
) {

  public static ChatMessageResponseWithUsedBookId of(
      long chatRoomId,
      String message,
      ContentType contentType,
      LocalDateTime createdAt,
      long usedBookId
  ) {
    return new ChatMessageResponseWithUsedBookId(
        chatRoomId,
        message,
        contentType,
        createdAt,
        usedBookId
    );
  }

  @QueryProjection
  public ChatMessageResponseWithUsedBookId(long chatRoomId, String message, ContentType contentType,
      LocalDateTime createdAt, long usedBookId) {
    this.chatRoomId = chatRoomId;
    this.message = message;
    this.contentType = contentType;
    this.createdAt = createdAt;
    this.usedBookId = usedBookId;
  }
}
