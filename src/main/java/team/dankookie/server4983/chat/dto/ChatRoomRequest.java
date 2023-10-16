package team.dankookie.server4983.chat.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatRoomRequest {
    private long usedBookId;

    public static ChatRoomRequest of(long usedBookId) {
        return ChatRoomRequest.builder().usedBookId(usedBookId).build();
    }

}
