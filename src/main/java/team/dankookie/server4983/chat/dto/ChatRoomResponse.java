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
    private long chatRoomId;

    private long buyerId;

    private String buyerNickname;

    private long sellerId;

    private String sellerNickname;

    private String position;

    public static ChatRoomResponse of(ChatRoom chatRoom , String userNickname) {
        String postition = "unknown";
        String buyerNickname = chatRoom.getBuyer().getNickname();
        String sellerNickname = chatRoom.getSeller().getNickname();
        if(buyerNickname.equals(userNickname)) {
            postition = "buyer";
        } else if(sellerNickname.equals(userNickname)) {
            postition = "seller";
        }

        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .buyerId(chatRoom.getBuyer().getId())
                .buyerNickname(buyerNickname)
                .sellerId(chatRoom.getSeller().getId())
                .sellerNickname(sellerNickname)
                .position(postition)
                .build();
    }

}
