package team.dankookie.server4983.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.SellerChat;

public interface SellerChatRepository extends JpaRepository<SellerChat, Long> {
}
