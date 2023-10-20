package team.dankookie.server4983.chat.repository;

import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepositoryCustom {

    List<SellerChat> getSellerChatting(long chatRoomId);

    List<ChatMessageResponse> getNotReadSellerChattingData(long chatRoomId);

    List<BuyerChat> getBuyerChatting(long chatRoomId);

    List<ChatMessageResponse> getNotReadBuyerChattingData(long chatRoomId);

    Member getSeller(long chatRoomId);

    long modifySellerChattingToRead(long chatRoomId);

    long modifyBuyerChattingToRead(long chatRoomId);

    Optional<ChatRoom> findChatRoomAndBookById(long chatRoomId);

    Optional<ChatRoom> findBookBySellerAndBuyerAndBook(Member seller, Member buyer, UsedBook usedBook);

    List<ChatListResponse> findByChatroomListWithNickname(String nickname);

    List<ChatMessageResponse> findChatMessageByChatroomIdWithBuyerNickname(long chatRoomId, String nickname);
    List<ChatMessageResponse> findChatMessageByChatroomIdWithSellerNickname(long chatRoomId, String nickname);

    void updateSellerChattingToRead(long chatRoomId);

    void updateBuyerChattingToRead(long chatRoomId);

    Member getBuyer(long chatRoomId);
}
