package team.dankookie.server4983.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponseWithUsedBookId;

@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
@Entity
public class SellerChat {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Enumerated(EnumType.STRING)
  private ContentType contentType;

  @ColumnDefault("false")
  @Builder.Default
  private Boolean isRead = false;

  @Column(nullable = false)
  private String message;

  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
  @CreationTimestamp
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;

  public static SellerChat buildSellerChat(String message, ContentType contentType) {
    return SellerChat.builder()
        .message(message)
        .contentType(contentType)
        .build();
  }

  public static SellerChat buildSellerChat(String message, ContentType contentType,
      ChatRoom chatRoom) {
    return SellerChat.builder()
        .message(message)
        .contentType(contentType)
        .chatRoom(chatRoom)
        .build();
  }

  public ChatMessageResponse toChatMessageResponse() {
    return ChatMessageResponse.of(
        chatRoom.getChatRoomId(),
        message,
        contentType,
        createdAt
    );
  }

  public ChatMessageResponseWithUsedBookId toChatMessageResponseWithUsedBookId(long usedBookId) {
    return ChatMessageResponseWithUsedBookId.of(
        chatRoom.getChatRoomId(),
        message,
        contentType,
        createdAt,
        usedBookId
    );
  }

  public void updateIsReadTrue() {
    this.isRead = true;
  }

}
