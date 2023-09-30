package team.dankookie.server4983.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member seller;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuyerChat> buyerChats = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SellerChat> sellerChats = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private UsedBook usedBook;

    public static ChatRoom buildChatRoom(Member buyer , Member seller , UsedBook usedBook) {
        return ChatRoom.builder().buyer(buyer).seller(seller).usedBook(usedBook).build();
    }

    public void addBuyerChat(BuyerChat chat) {
        if (this.getBuyerChats() == null) {
            this.buyerChats = new ArrayList<>();
            this.getBuyerChats().add(chat);
        }else {
            this.getBuyerChats().add(chat);
        }
    }

    public void addSellerChat(SellerChat chat) {
        if (this.getSellerChats() == null) {
            this.sellerChats = new ArrayList<>();
            this.getSellerChats().add(chat);
        }else {
            this.getSellerChats().add(chat);
        }
    }
}
