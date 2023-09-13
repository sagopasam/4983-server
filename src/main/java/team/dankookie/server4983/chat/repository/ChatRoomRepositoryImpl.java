package team.dankookie.server4983.chat.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.member.domain.Member;

import static team.dankookie.server4983.member.domain.QMember.member;
import static team.dankookie.server4983.chat.domain.QBuyerChat.buyerChat;
import static team.dankookie.server4983.chat.domain.QSellerChat.sellerChat;
import static team.dankookie.server4983.chat.domain.QChatRoom.chatRoom;
import java.util.List;

@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SellerChat> getSellerChatting(long chatRoomId) {
        return jpaQueryFactory.select(sellerChat)
                .from(chatRoom)
                .innerJoin(chatRoom.sellerChats , sellerChat)
                .where(chatRoom.chatRoomId.eq(chatRoomId))
                .fetch();
    }

    @Override
    public List<BuyerChat> getBuyerChatting(long chatRoomId) {
        return jpaQueryFactory.select(buyerChat)
                .from(chatRoom)
                .innerJoin(chatRoom.buyerChats , buyerChat)
                .where(chatRoom.chatRoomId.eq(chatRoomId))
                .fetch();
    }

    @Override
    public Member getSeller(long chatRoomId) {
        return jpaQueryFactory.select(member)
                .from(chatRoom)
                .innerJoin(chatRoom.seller , member)
                .where(chatRoom.chatRoomId.eq(chatRoomId))
                .fetchOne();
    }
}
