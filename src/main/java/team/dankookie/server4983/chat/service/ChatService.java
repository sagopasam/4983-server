package team.dankookie.server4983.chat.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.*;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.handler.ChatBotAdmin;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.service.MemberService;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

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
    private final ChatBotAdmin chatBotAdmin;

    public void chatRequestHandler(ChatRequest chatRequest , AccessToken accessToken) {
        String userName = accessToken.nickname();
        Member member = memberService.findMemberByNickname(userName);

        chatLogicHandler.chatLoginHandler(chatRequest , member);
    }

    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest , AccessToken accessToken) throws AccountException {
        String nickname = accessToken.nickname();

        UsedBook usedBook = usedBookRepository.findById(chatRoomRequest.getSalesPost())
                .orElseThrow(() -> new ChatException("거래 글을 찾을 수 없습니다."));
        Member seller = usedBook.getSellerMember();
        Member buyer = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if(seller.getNickname().equals(nickname)) {
            throw new ChatException("자신의 판매글에 거래요청을 할 수 없습니다.");
        }

        Optional<ChatRoom> result = chatRoomRepository.findBookBySellerAndBuyerAndBook(seller , buyer , usedBook);
        if(result.isPresent()) {
            return ChatRoomResponse.of(result.get() , nickname);
        }
        ChatRoom chatRoom = buildChatRoom(buyer , seller , usedBook);

        ChatRoom savedChatroom = chatRoomRepository.save(chatRoom);

        ChatRequest chatRequest = ChatRequest.builder()
                .chatRoomId(savedChatroom.getChatRoomId())
                .contentType(ContentType.BOOK_PURCHASE_START)
                .build();
        chatRequestHandler(chatRequest , accessToken);

        return ChatRoomResponse.of(savedChatroom , nickname);
    }

    public ChatRoomResponse getChatRoom(long chatRoom , HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtTokenUtils.getNickname(token , tokenSecretKey.getSecretKey());

        ChatRoom result = chatRoomRepository.findById(chatRoom)
                .orElseThrow(() -> new ChatException("존재하지 않는 채팅방 입니다."));

        return ChatRoomResponse.of(result , userName);
    }

    @Transactional
    public List<ChatMessageResponse> getChattingData(long chatRoomId, AccessToken accessToken) {

        String nickname = accessToken.nickname();

        boolean isMemberBuyer = chatRoomRepository.existsByChatRoomIdAndBuyer_Nickname(chatRoomId, nickname);

        if (!isMemberBuyer) {
            boolean isMemberSeller = chatRoomRepository.existsByChatRoomIdAndSeller_Nickname(chatRoomId, nickname);

            if (!isMemberSeller) {
                throw new ChatException("채팅방에 속해있지 않습니다.");
            } else {
                chatRoomRepository.updateSellerChattingToRead(chatRoomId);
                return chatRoomRepository.findChatMessageByChatroomIdWithSellerNickname(chatRoomId, nickname);
            }
        }else {
            chatRoomRepository.updateBuyerChattingToRead(chatRoomId);
            return chatRoomRepository.findChatMessageByChatroomIdWithBuyerNickname(chatRoomId, nickname);
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

    public void stopTrade(ChatRequest chatRequest) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRequest.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        Member seller = chatRoomRepository.getSeller(chatRequest.getChatRoomId());
        Member buyer = chatRoomRepository.getBuyer(chatRequest.getChatRoomId());
        String target = chatRequest.getData().get("target").toString();

        if(target.equals("buyer")) {
            chatBotAdmin.tradeStopByBuyer(chatRoom , seller , buyer);
        } else if(target.equals("seller")) {
            chatBotAdmin.tradeStopBySeller(chatRoom , seller , buyer);
        } else {
            return;
        }
    }
}
