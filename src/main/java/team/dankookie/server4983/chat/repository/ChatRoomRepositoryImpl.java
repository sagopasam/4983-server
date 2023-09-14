package team.dankookie.server4983.chat.repository;

import com.querydsl.jpa.JPAExpressions;
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
    public List<SellerChat> getNotReadSellerChattingData(long chatRoomId) {
        return jpaQueryFactory.select(sellerChat)
                .from(chatRoom)
                .innerJoin(chatRoom.sellerChats , sellerChat).on(sellerChat.isRead.eq(false))
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
    public List<BuyerChat> getNotReadBuyerChattingData(long chatRoomId) {
        return jpaQueryFactory.select(buyerChat)
                .from(chatRoom)
                .innerJoin(chatRoom.buyerChats , buyerChat).on(buyerChat.isRead.eq(false))
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

    @Override
    public long modifySellerChattingToRead(long chatRoomId) {
        return jpaQueryFactory.update(sellerChat)
                .set(sellerChat.isRead , true)
                .where(sellerChat.id.in(
                        JPAExpressions.select(sellerChat.id)
                                .from(chatRoom)
                                .innerJoin(chatRoom.sellerChats , sellerChat)
                                .where(chatRoom.chatRoomId.eq(chatRoomId))
                ).and(sellerChat.isRead.eq(false)))
                .execute();
    }

    @Override
    public long modifyBuyerChattingToRead(long chatRoomId) {
        return jpaQueryFactory.update(buyerChat)
                .set(buyerChat.isRead , true)
                .where(buyerChat.id.in(
                        JPAExpressions.select(buyerChat.id)
                                .from(chatRoom)
                                .innerJoin(chatRoom.buyerChats , buyerChat)
                                .where(chatRoom.chatRoomId.eq(chatRoomId))
                ).and(buyerChat.isRead.eq(false)))
                .execute();
    }

}
