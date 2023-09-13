package team.dankookie.server4983.chat.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.common.domain.BaseEntity;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class BuyerChat extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false , columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean isRead = false;

    @Column(nullable = false)
    private String message;

}
