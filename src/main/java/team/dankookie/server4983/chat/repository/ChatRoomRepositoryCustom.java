package team.dankookie.server4983.chat.repository;

import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.SellerChat;

import java.util.List;

public interface ChatRoomRepositoryCustom {

    List<SellerChat> getSellerChatting(long chatRoomId);

    List<BuyerChat> getBuyerChatting(long chatRoomId);

}
