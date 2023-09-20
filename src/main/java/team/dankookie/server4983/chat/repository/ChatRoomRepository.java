package team.dankookie.server4983.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.dto.ChatListResponse;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomRepositoryCustom {

}
