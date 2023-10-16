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
    private Long usedBookId;

    public static ChatRoomRequest of(Long usedBookId) {
        return ChatRoomRequest.builder().usedBookId(usedBookId).build();
    }

}
