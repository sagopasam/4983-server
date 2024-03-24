package team.dankookie.server4983.chat.handler;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.repository.BuyerChatRepository;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.chat.repository.SellerChatRepository;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;
import team.dankookie.server4983.member.repository.MemberRepository;

@SpringBootTest
class ChatLogicHandlerTest {

    @Autowired
    private ChatLogicHandler sut;

    @Autowired
    private SellerChatRepository sellerChatRepository;

    @Autowired
    private BuyerChatRepository buyerChatRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UsedBookRepository usedBookRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @MockBean
    private FcmService fcmService;

    @MockBean
    private LockerRepository lockerRepository;

    @Test
    void chatLogicInCancelStep() {
        // given
        String studentId = "studentId";
        Member member = MemberFixture.createMemberByStudentIdAndNickname(studentId, "testNickname");
        Member otherMember = MemberFixture.createMemberByStudentIdAndNickname("otherStudentId", "otherNickname");
        memberRepository.save(member);
        memberRepository.save(otherMember);

        UsedBook usedBook1 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("사회과학통계방법")
                .buyerMember(otherMember)
                .sellerMember(member)
                .price(1000)
                .tradeAvailableDatetime(LocalDateTime.now())
                .build();
        usedBookRepository.save(usedBook1);

        UsedBook usedBook2 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("사회과학통계방법")
                .buyerMember(member)
                .sellerMember(otherMember)
                .price(1000)
                .tradeAvailableDatetime(LocalDateTime.now())
                .build();
        usedBookRepository.save(usedBook2);

        ChatRoom chatRoom = ChatRoom.buildChatRoom(member, otherMember, usedBook1);
        final ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRequest chatRequest = ChatRequest.of(savedChatRoom.getChatRoomId(), ContentType.CANCEL);

        doNothing().when(fcmService).sendChattingNotificationByToken(any());
        when(lockerRepository.findByChatRoom(any())).thenReturn(
                Optional.of(Locker.builder()
                        .chatRoom(savedChatRoom)
                        .isExists(false)
                        .lockerNumber(0)
                        .password("1234")
                        .tradeDate(LocalDate.now())
                        .build())
        );

        // when
        sut.chatLogic(chatRequest);

        // then
        final List<SellerChat> sellerChats = sellerChatRepository.findAll();
        final List<BuyerChat> buyerChats = buyerChatRepository.findAll();
        final ChatRoom result = chatRoomRepository.findByChatRoomId(1).get();

        assertThat(sellerChats).hasSize(1);
        assertThat(buyerChats).hasSize(1);
        assertThat(result.getInteractStep()).isEqualTo(-1);
    }
}
