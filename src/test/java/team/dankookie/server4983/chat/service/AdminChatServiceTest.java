package team.dankookie.server4983.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;
import team.dankookie.server4983.member.repository.MemberRepository;

@SpringBootTest
@Transactional
@TestMethodOrder(OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class AdminChatServiceTest {

    @Autowired
    AdminChatService adminChatService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    ChatService chatService;

    @Autowired
    UsedBookRepository usedBookRepository;

    @Autowired
    MemberRepository memberRepository;

    @MockBean
    FcmService fcmService;

    @Test
    @Order(1)
    @DisplayName("관리자가 판매자에게 메세지를 보낸다.")
    void postChattingToSeller() {

        //given
        final long chatRoomId = 1L;
        final long usedBookId = 1L;
        final String buyerId = "buyer";
        final String sellerId = "seller";
        final LocalDateTime now = LocalDateTime.now();
        final ChatRoomRequest chatRoomRequest = ChatRoomRequest.of(usedBookId);

        Member buyer = MemberFixture.createMemberByStudentId(buyerId);
        Member seller = MemberFixture.createMemberByStudentIdAndNickname(sellerId,
                "seller");

        memberRepository.save(buyer);
        memberRepository.save(seller);

        UsedBook usedBook = UsedBook.builder()
                .name("C언어")
                .buyerMember(buyer)
                .sellerMember(seller)
                .bookStatus(BookStatus.SALE)
                .price(100)
                .tradeAvailableDatetime(now)
                .build();
        usedBookRepository.save(usedBook);

        doNothing().when(fcmService).sendChattingNotificationByToken(any());
        chatService.createChatRoom(chatRoomRequest, AccessToken.of("test", buyerId));

        //when
        adminChatService.postSellerChat(chatRoomId, "[관리자 메세지] 관리자 메세지가 왔습니다.");

        //then
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        assertThat(chatRoom.getSellerChats().get(2).getMessage()).isEqualTo("[관리자 메세지] 관리자 메세지가 왔습니다.");
    }

    @Test
    @Order(2)
    @DisplayName("관리자가 구매자에게 메세지를 보낸다.")
    void postChattingToBuyer() {

        //given
        final long chatRoomId = 2L;
        final long usedBookId = 2L;
        final String buyerId = "buyer";
        final String sellerId = "seller";
        final LocalDateTime now = LocalDateTime.now();
        final ChatRoomRequest chatRoomRequest = ChatRoomRequest.of(usedBookId);

        Member buyer = MemberFixture.createMemberByStudentId(buyerId);
        Member seller = MemberFixture.createMemberByStudentIdAndNickname(sellerId,
                "seller");

        memberRepository.save(buyer);
        memberRepository.save(seller);

        UsedBook usedBook = UsedBook.builder()
                .name("C언어")
                .buyerMember(buyer)
                .sellerMember(seller)
                .bookStatus(BookStatus.SALE)
                .price(100)
                .tradeAvailableDatetime(now)
                .build();

        usedBookRepository.save(usedBook);

        doNothing().when(fcmService).sendChattingNotificationByToken(any());
        chatService.createChatRoom(chatRoomRequest, AccessToken.of("test", buyerId));

        //when
        adminChatService.postBuyerChat(chatRoomId, "[관리자 메세지] 관리자 메세지가 왔습니다.");

        //then
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        assertThat(chatRoom.getBuyerChats().get(2).getMessage()).isEqualTo("[관리자 메세지] 관리자 메세지가 왔습니다.");
    }
}


