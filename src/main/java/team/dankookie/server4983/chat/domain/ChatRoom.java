package team.dankookie.server4983.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.member.domain.Member;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member seller;

    @OneToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE }, orphanRemoval = true)
    private List<BuyerChat> buyerChats;

    @OneToMany(cascade = {CascadeType.PERSIST , CascadeType.REMOVE }, orphanRemoval = true)
    private List<SellerChat> sellerChats;

    @OneToOne(fetch = FetchType.LAZY)
    private UsedBook usedBook;

    public static ChatRoom buildChatRoom(Member buyer , Member seller , UsedBook usedBook) {
        return ChatRoom.builder().buyer(buyer).seller(seller).usedBook(usedBook).build();
    }
}
