package team.dankookie.server4983.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatResponse {

    private String message;

    private String session;

    public static ChatResponse createChatResponse(String message , ChatRequest chatMessage) {
        return ChatResponse.builder()
                .message(message)
                .session(chatMessage.getSession())
                .build();
    }

    public static ChatResponse createChatResponse(String message , String session) {
        return ChatResponse.builder()
                .message(message)
                .session(session)
                .build();
    }

}
