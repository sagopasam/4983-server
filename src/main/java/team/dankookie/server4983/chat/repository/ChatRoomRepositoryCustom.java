package team.dankookie.server4983.chat.repository;

import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.member.domain.Member;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<SellerChat> getSellerChatting(long chatRoomId);

    List<SellerChat> getNotReadSellerChattingData(long chatRoomId);

    List<BuyerChat> getBuyerChatting(long chatRoomId);

    List<BuyerChat> getNotReadBuyerChattingData(long chatRoomId);

    Member getSeller(long chatRoomId);

    long modifySellerChattingToRead(long chatRoomId);

    long modifyBuyerChattingToRead(long chatRoomId);

}
