package team.dankookie.server4983.chat.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.common.domain.BaseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SellerChat {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isRead = false;

    @Column(nullable = false)
    private String message;

    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @CreatedDate
    private LocalDateTime createdAt;

    public static SellerChat buildSellerChat(String message , ContentType contentType) {
        return SellerChat.builder()
                .message(message)
                .contentType(contentType)
                .build();
    }

}
