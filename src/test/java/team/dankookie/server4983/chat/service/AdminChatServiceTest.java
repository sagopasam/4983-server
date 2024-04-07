package team.dankookie.server4983.chat.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import team.dankookie.server4983.chat.dto.ChatRoomResponse;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;
import team.dankookie.server4983.member.repository.MemberRepository;

@SpringBootTest
@Transactional
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
    @DisplayName("관리자가 판매자에게 메세지를 보낸다.")
    void postChattingToSeller() {

        //given
        final String buyerId = "buyer";
        final String sellerId = "seller";
        final LocalDateTime now = LocalDateTime.now();

        Member buyer = MemberFixture.createMemberByStudentId(buyerId);
        Member seller = MemberFixture.createMemberByStudentIdAndNickname(sellerId,
                "seller");

        memberRepository.save(buyer);
        memberRepository.save(seller);
        UsedBook savedBook = usedBookRepository.save(createBook(buyer, seller, now));

        ChatRoomRequest chatRoomRequest = ChatRoomRequest.of(savedBook.getId());

        doNothing().when(fcmService).sendChattingNotificationByToken(any());
        ChatRoomResponse savedChatRoom = chatService.createChatRoom(chatRoomRequest, AccessToken.of("test", buyerId));

        //when
        adminChatService.postSellerChat(savedChatRoom.getChatRoomId(), "[관리자 메세지] 관리자 메세지가 왔습니다.");

        //then
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(savedChatRoom.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        assertThat(chatRoom.getSellerChats().get(2).getMessage()).isEqualTo("[관리자 메세지] 관리자 메세지가 왔습니다.");
    }


    @Test
    @DisplayName("관리자가 구매자에게 메세지를 보낸다.")
    void postChattingToBuyer() {

        //given
        final String buyerId = "buyer";
        final String sellerId = "seller";
        final LocalDateTime now = LocalDateTime.now();

        Member buyer = MemberFixture.createMemberByStudentId(buyerId);
        Member seller = MemberFixture.createMemberByStudentIdAndNickname(sellerId,
                "seller");

        memberRepository.save(buyer);
        memberRepository.save(seller);
        UsedBook savedBook = usedBookRepository.save(createBook(buyer, seller, now));

        ChatRoomRequest chatRoomRequest = ChatRoomRequest.of(savedBook.getId());

        doNothing().when(fcmService).sendChattingNotificationByToken(any());
        ChatRoomResponse savedChatRoom = chatService.createChatRoom(chatRoomRequest, AccessToken.of("test", buyerId));

        //when
        adminChatService.postBuyerChat(savedChatRoom.getChatRoomId(), "[관리자 메세지] 관리자 메세지가 왔습니다.");

        //then
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(savedChatRoom.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        assertThat(chatRoom.getBuyerChats().get(2).getMessage()).isEqualTo("[관리자 메세지] 관리자 메세지가 왔습니다.");
    }

    private UsedBook createBook(Member buyer, Member seller, LocalDateTime now) {
        return UsedBook.builder()
                .name("C언어")
                .buyerMember(buyer)
                .sellerMember(seller)
                .bookStatus(BookStatus.SALE)
                .price(100)
                .tradeAvailableDatetime(now)
                .build();
    }
}


