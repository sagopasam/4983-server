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
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.service.MemberService;

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
    private final JwtTokenUtils jwtTokenUtils;
    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String key;

    @Transactional
    public void chatRequestHandler(ChatRequest chatRequest , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtTokenUtils.getNickname(token , key);
        Member member = memberService.findMemberByNickname(userName);

        chatLogicHandler.chatLoginHandler(chatRequest , member);
    }

    @Transactional
    public Long createChatRoom(ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        String token = request.getHeader("Authorization").substring(7);

        String userName = jwtTokenUtils.getNickname(token , key);

        /** FIXME

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
        UsedBook usedBook = usedBookRepository.save(new UsedBook(30L , "bookName" , 400 , LocalDate.now() , "publisher" , College.LAW , Department.ACCOUNTING , BookStatus.SALE ,false , false , false, buyer , seller));

        ChatRoom chatRoom = buildChatRoom(buyer , seller , usedBook);

        return chatRoomRepository.save(chatRoom).getChatRoomId();
    }

    @Transactional
    public Object getChattingData(long chatRoomId , String type) {
        if(type.equals("seller")) {
            chatRoomRepository.modifySellerChattingToRead(chatRoomId);
            List<SellerChat> result = chatRoomRepository.getSellerChatting(chatRoomId);

            return result;
        } else if(type.equals("buyer")) {
            chatRoomRepository.modifyBuyerChattingToRead(chatRoomId);
            List<BuyerChat> result = chatRoomRepository.getBuyerChatting(chatRoomId);

            return result;
        } else {
            throw new ChatException("잘못된 타입입니다. seller , buyer 중 하나만 선택해주세요.");
        }
    }

    @Transactional
    public Object getNotReadChattingData(long chatRoomId, String type) {
        if(type.equals("seller")) {
            List<SellerChat> result = chatRoomRepository.getNotReadSellerChattingData(chatRoomId);
            chatRoomRepository.modifySellerChattingToRead(chatRoomId);

            return result;
        } else if(type.equals("buyer")) {
            List<BuyerChat> result = chatRoomRepository.getNotReadBuyerChattingData(chatRoomId);
            chatRoomRepository.modifyBuyerChattingToRead(chatRoomId);

            return result;
        } else {
            throw new ChatException("잘못된 타입입니다. seller , buyer 중 하나만 선택해주세요.");
        }
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
