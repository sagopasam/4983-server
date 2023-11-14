package team.dankookie.server4983.chat.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.AdminChatMessageResponse;
import team.dankookie.server4983.chat.dto.AdminChatRoomListResponse;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.member.domain.Member;

public interface ChatRoomRepositoryCustom {

  List<SellerChat> getSellerChatting(long chatRoomId);

  List<ChatMessageResponse> getNotReadSellerChattingData(long chatRoomId);

  List<BuyerChat> getBuyerChatting(long chatRoomId);

  List<ChatMessageResponse> getNotReadBuyerChattingData(long chatRoomId);

  Member getSeller(long chatRoomId);

  long modifySellerChattingToRead(long chatRoomId);

  long modifyBuyerChattingToRead(long chatRoomId);

  Optional<ChatRoom> findChatRoomAndBookById(long chatRoomId);

  Optional<ChatRoom> findBookBySellerAndBuyerAndBook(Member seller, Member buyer,
      UsedBook usedBook);

  List<ChatListResponse> findByChatroomListWithStudentId(String studentId);

  List<ChatMessageResponse> findChatMessageByChatroomIdWithBuyerStudentId(long chatRoomId,
      String studentId);

  List<ChatMessageResponse> findChatMessageByChatroomIdWithSellerStudentId(long chatRoomId,
      String studentId);

  void updateSellerChattingToRead(long chatRoomId);

  void updateBuyerChattingToRead(long chatRoomId);

  Member getBuyer(long chatRoomId);

  Page<AdminChatRoomListResponse> getAdminChatList(Pageable pageable, String searchKeyword,
      int interact);

  List<AdminChatMessageResponse> findBuyerChatListByChatRoomId(Long chatRoomId);

  List<AdminChatMessageResponse> findSellerChatListByChatRoomId(Long chatRoomId);

}

