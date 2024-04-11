package team.dankookie.server4983.chat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.chat.constant.ContentType;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminChatMessageRequest {

    private String message;
    private ContentType contentType;

}
