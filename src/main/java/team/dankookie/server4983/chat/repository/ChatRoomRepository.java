package team.dankookie.server4983.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomRepositoryCustom {
    boolean existsByChatRoomIdAndBuyer_Nickname(long chatRoomId, String nickname);

    boolean existsByChatRoomIdAndSeller_Nickname(long chatRoomId, String nickname);

}
