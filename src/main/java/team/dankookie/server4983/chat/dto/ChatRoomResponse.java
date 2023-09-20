package team.dankookie.server4983.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.member.domain.Member;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatRoomResponse {
    long roomId;

    long buyerId;

    String buyerNickname;

    long sellerId;

    String sellerNickname;

    public static ChatRoomResponse of(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .roomId(chatRoom.getChatRoomId())
                .buyerId(chatRoom.getBuyer().getId())
                .buyerNickname(chatRoom.getBuyer().getNickname())
                .sellerId(chatRoom.getSeller().getId())
                .sellerNickname(chatRoom.getSeller().getNickname())
                .build();
    }

}
