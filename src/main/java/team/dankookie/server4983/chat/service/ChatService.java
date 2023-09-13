package team.dankookie.server4983.chat.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

import javax.security.auth.login.AccountException;
import java.time.LocalDate;
import java.util.List;

import static team.dankookie.server4983.chat.domain.ChatRoom.buildChatRoom;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatLogicHandler chatLogicHandler;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final UsedBookRepository usedBookRepository;
    @Value("${jwt.secret-key}")
    private String key;

    @Transactional
    public String chatRequestHandler(ChatRequest chatRequest , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = JwtTokenUtils.getNickname(token , key);

        return chatLogicHandler.chatLoginHandler(chatRequest , userName);
    }

    @Transactional
    public Long createChatRoom(ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        String token = request.getHeader("Authorization").substring(7);

        String userName = JwtTokenUtils.getNickname(token , key);

        /** Fix-Me

         - 추가 구현 -

         거래 ID -> 거래 글 가져오기
         거래 글 -> 책 정보와 구매자 정보 추출
         책 정보와 구매자 정보 추출

        */

        // 임시 판매자
        Member seller = memberRepository.findByStudentId("testStudentId")
                .orElseGet(() -> createTemporaryMember());
        memberRepository.save(seller);

        // 구매자
        Member buyer = memberRepository.findByNickname(userName)
                .orElseThrow(() -> new AccountException("판매자 정보를 찾을 수 없습니다."));

        // 임시 책 정보
        UsedBook usedBook = new UsedBook("bookName" , 400 , LocalDate.now() , BookStatus.SALE , seller , Department.ACCOUNTING , College.LAW);
        usedBookRepository.save(usedBook);

        ChatRoom chatRoom = buildChatRoom(buyer , seller , usedBook);

        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    public List<SellerChat> getSellerChatting(long chatRoomId) {
        return chatRoomRepository.getSellerChatting(chatRoomId);
    }

    public List<BuyerChat> getBuyerChatting(long chatRoomId) {
        return chatRoomRepository.getBuyerChatting(chatRoomId);
    }

    public Member createTemporaryMember() {
        return Member.builder()
                .studentId("studentIds")
                .yearOfAdmission(0)
                .department(Department.DEPARTMENT_OF_LAW)
                .nickname("DFGgt4t21Rr-351rfvZCVb")
                .password("password")
                .phoneNumber("01012341234")
                .accountHolder("accountHolder")
                .accountBank(AccountBank.IBK)
                .accountNumber("0101010100101010")
                .build();
    }
}
