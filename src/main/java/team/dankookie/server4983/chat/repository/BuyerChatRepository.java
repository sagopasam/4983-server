package team.dankookie.server4983.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;

public interface BuyerChatRepository extends JpaRepository<BuyerChat, Long> {

  List<BuyerChat> findAllByChatRoom(ChatRoom chatRoom);
}
