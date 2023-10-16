package team.dankookie.server4983.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;

import java.util.Optional;
import team.dankookie.server4983.member.domain.Member;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> , ChatRoomRepositoryCustom {
    boolean existsByChatRoomIdAndBuyer_Nickname(long chatRoomId, String nickname);

    boolean existsByChatRoomIdAndSeller_Nickname(long chatRoomId, String nickname);

    boolean existsByBuyer_NicknameOrSeller_NicknameAndChatRoomId(String buyerNickname,
        String sellerNickname, Long chatRoomId);

    Optional<ChatRoom> findByChatRoomId(long chatRoomId);

    Optional<ChatRoom> findBySellerOrBuyerAndChatRoomId(Member seller, Member buyer, Long chatRoomId);
}
