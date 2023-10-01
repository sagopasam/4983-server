package team.dankookie.server4983.chat.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.domain.QMember;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static team.dankookie.server4983.book.domain.QBookImage.bookImage;
import static team.dankookie.server4983.book.domain.QUsedBook.usedBook;
import static team.dankookie.server4983.chat.domain.QBuyerChat.buyerChat;
import static team.dankookie.server4983.chat.domain.QChatRoom.chatRoom;
import static team.dankookie.server4983.chat.domain.QSellerChat.sellerChat;
import static team.dankookie.server4983.member.domain.QMember.member;

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

    @Override
    public Optional<ChatRoom> findChatRoomAndBookById(long chatRoomId) {
        ChatRoom result = jpaQueryFactory.select(chatRoom)
                .from(chatRoom)
                .innerJoin(chatRoom.usedBook).fetchJoin()
                .where(chatRoom.chatRoomId.eq(chatRoomId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<ChatRoom> findBookBySellerAndBuyerAndBook(Member seller, Member buyer, UsedBook usedBookData) {
        QMember QSeller = new QMember("seller");

        ChatRoom result = jpaQueryFactory.select(chatRoom)
                .from(chatRoom)
                .innerJoin(chatRoom.usedBook , usedBook).on(usedBook.eq(usedBookData))
                .innerJoin(chatRoom.buyer , member).on(member.eq(buyer))
                .innerJoin(chatRoom.seller , QSeller).on(QSeller.eq(seller))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<ChatListResponse> findByChatroomWithNickname(String nickname) {
        List<ChatListResponse> chatListResponseList = jpaQueryFactory
                .select(
                        Projections.constructor(
                                ChatListResponse.class,
                                chatRoom.chatRoomId,
                                usedBook.name,
                                getLastBuyerMessage(),
                                getLastBuyerMessageCreatedAt(),
                                getLastBuyerMessageIsRead(),
                                getFirstUsedBookImageUrl()
                        ))
                .from(chatRoom)
                .join(chatRoom.usedBook, usedBook)
                .where(chatRoom.buyer.nickname.eq(nickname))
                .fetch();

        chatListResponseList.addAll(jpaQueryFactory
                .select(
                        Projections.constructor(
                                ChatListResponse.class,
                                chatRoom.chatRoomId,
                                usedBook.name,
                                getLastSellerMessage(),
                                getLastSellerMessageCreatedAt(),
                                getLastSellerMessageIsRead(),
                                getFirstUsedBookImageUrl()
                        ))
                .from(chatRoom)
                .join(chatRoom.usedBook, usedBook)
                .where(chatRoom.seller.nickname.eq(nickname))
                .fetch());

        if (chatListResponseList.size() == 0) {
            return chatListResponseList;
        }
        chatListResponseList.sort(Comparator.comparing(ChatListResponse::createdAt).reversed());
        return chatListResponseList;
    }

    @Override
    public List<ChatMessageResponse> findChatMessageByChatroomIdWithBuyerNickname(long chatRoomId, String nickname) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ChatMessageResponse.class,
                        buyerChat.message,
                        buyerChat.contentType,
                        buyerChat.createdAt
                )).from(buyerChat)
                .where(buyerChat.chatRoom.chatRoomId.eq(chatRoomId),
                        buyerChat.chatRoom.buyer.nickname.eq(nickname)
                )
                .orderBy(buyerChat.createdAt.asc())
                .fetch();
    }

    @Override
    public List<ChatMessageResponse> findChatMessageByChatroomIdWithSellerNickname(long chatRoomId, String nickname) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ChatMessageResponse.class,
                        sellerChat.message,
                        sellerChat.contentType,
                        sellerChat.createdAt
                )).from(sellerChat)
                .where(sellerChat.chatRoom.chatRoomId.eq(chatRoomId),
                        sellerChat.chatRoom.seller.nickname.eq(nickname)
                ).orderBy(sellerChat.createdAt.asc())
                .fetch();
    }

    @Override
    public void updateSellerChattingToRead(long chatRoomId) {
        jpaQueryFactory.update(sellerChat)
                .set(sellerChat.isRead, true)
                .where(sellerChat.chatRoom.chatRoomId.eq(chatRoomId))
                .execute();
    }

    @Override
    public void updateBuyerChattingToRead(long chatRoomId) {
        jpaQueryFactory.update(buyerChat)
                .set(buyerChat.isRead, true)
                .where(buyerChat.chatRoom.chatRoomId.eq(chatRoomId))
                .execute();
    }

    @Override
    public Member getBuyer(long chatRoomId) {
        return jpaQueryFactory.select(member)
                .from(chatRoom)
                .innerJoin(chatRoom.buyer , member)
                .where(chatRoom.chatRoomId.eq(chatRoomId))
                .fetchOne();
    }

    private static JPQLQuery<Boolean> getLastSellerMessageIsRead() {
        return JPAExpressions.select(sellerChat.isRead)
                .from(sellerChat)
                .where(sellerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(sellerChat.createdAt.desc())
                .limit(1);
    }

    private static JPQLQuery<LocalDateTime> getLastSellerMessageCreatedAt() {
        return JPAExpressions.select(sellerChat.createdAt)
                .from(sellerChat)
                .where(sellerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(sellerChat.createdAt.desc())
                .limit(1);
    }

    private static JPQLQuery<String> getLastSellerMessage() {
        return JPAExpressions.select(sellerChat.message)
                .from(sellerChat)
                .where(sellerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(sellerChat.createdAt.desc())
                .limit(1);
    }

    private static JPQLQuery<String> getFirstUsedBookImageUrl() {
        return JPAExpressions.select(bookImage.imageUrl)
                .from(bookImage)
                .where(bookImage.usedBook.id.eq(usedBook.id))
                .orderBy(bookImage.id.asc())
                .limit(1);
    }

    private static JPQLQuery<Boolean> getLastBuyerMessageIsRead() {
        return JPAExpressions.select(buyerChat.isRead)
                .from(buyerChat)
                .where(buyerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(buyerChat.createdAt.desc())
                .limit(1);
    }

    private static JPQLQuery<LocalDateTime> getLastBuyerMessageCreatedAt() {
        return JPAExpressions.select(buyerChat.createdAt)
                .from(buyerChat)
                .where(buyerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(buyerChat.createdAt.desc())
                .limit(1);
    }

    private static JPQLQuery<String> getLastBuyerMessage() {
        return JPAExpressions.select(buyerChat.message)
                .from(buyerChat)
                .where(buyerChat.chatRoom.chatRoomId.eq(chatRoom.chatRoomId))
                .orderBy(buyerChat.createdAt.desc())
                .limit(1);
    }

}
