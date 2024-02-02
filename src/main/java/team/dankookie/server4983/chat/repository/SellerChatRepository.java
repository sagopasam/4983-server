package team.dankookie.server4983.chat.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;

public interface SellerChatRepository extends JpaRepository<SellerChat, Long> {

  List<SellerChat> findAllByChatRoom(ChatRoom chatRoom);
}
