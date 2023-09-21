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
    private final MemberService memberService;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenSecretKey tokenSecretKey;

    @Transactional
    public void chatRequestHandler(ChatRequest chatRequest , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtTokenUtils.getNickname(token , tokenSecretKey.getSecretKey());
        Member member = memberService.findMemberByNickname(userName);

        chatLogicHandler.chatLoginHandler(chatRequest , member);
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest , HttpServletRequest request) throws AccountException {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtTokenUtils.getNickname(token , key);

        UsedBook usedBook = usedBookRepository.findById(chatRoomRequest.getSalesPost())
                .orElseThrow(() -> new ChatException("거래 글을 찾을 수 없습니다."));
        Member seller = usedBook.getSellerMember();
        Member buyer = memberRepository.findByNickname(userName)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if(seller.getNickname().equals(userName)) {
            throw new ChatException("자신의 판매글에 거래요청을 할 수 없습니다.");
        }

        Optional<ChatRoom> result = chatRoomRepository.findBookBySellerAndBuyerAndBook(seller , buyer , usedBook);
        if(result.isPresent()) {
            return ChatRoomResponse.of(result.get() , userName);
        }
        ChatRoom chatRoom = buildChatRoom(buyer , seller , usedBook);

        return ChatRoomResponse.of(chatRoomRepository.save(chatRoom) , userName);
    }

    public ChatRoomResponse getChatRoom(long chatRoom , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtTokenUtils.getNickname(token , key);

        ChatRoom result = chatRoomRepository.findById(chatRoom)
                .orElseThrow(() -> new ChatException("존재하지 않는 채팅방 입니다."));

        return ChatRoomResponse.of(result , userName);
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

    public List<ChatListResponse> getChatListWithAccessToken(AccessToken accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken.value() , tokenSecretKey.getSecretKey());

        return chatRoomRepository.findByChatroomWithNickname(nickname);
    }

    private Member createTemporaryMember() {
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
