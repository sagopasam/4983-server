package team.dankookie.server4983.chat.service;

import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PURCHASE_START;
import static team.dankookie.server4983.chat.constant.ContentType.CUSTOM_BUYER;
import static team.dankookie.server4983.chat.constant.ContentType.CUSTOM_SELLER;
import static team.dankookie.server4983.chat.domain.ChatRoom.buildChatRoom;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import javax.security.auth.login.AccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.dto.ChatMessageRequest;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponseWithUsedBookId;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatRoomRequest;
import team.dankookie.server4983.chat.dto.ChatRoomResponse;
import team.dankookie.server4983.chat.dto.ChatStopRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.handler.ChatBotAdmin;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.repository.BuyerChatRepository;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.chat.repository.SellerChatRepository;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.service.MemberService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatLogicHandler chatLogicHandler;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final UsedBookRepository usedBookRepository;
    private final MemberService memberService;
    private final JwtTokenUtils jwtTokenUtils;
    private final ChatBotAdmin chatBotAdmin;
    private final LockerRepository lockerRepository;
    private final BuyerChatRepository buyerChatRepository;
    private final SellerChatRepository sellerChatRepository;

    public ChatRoom findByChatRoomId(long chatRoomId) {
        return chatRoomRepository.findByChatRoomId(chatRoomId)
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
    }

    public List<ChatMessageResponse> chatRequestHandler(ChatRequest chatRequest,
                                                        AccessToken accessToken) {

        Member member = memberService.findMemberByStudentId(accessToken.studentId());

        ChatRoom chatRoom = chatRoomRepository.findBySellerOrBuyerAndChatRoomId(member.getId(), member.getId(),
                        chatRequest.getChatRoomId())
                .orElseThrow(() -> new ChatException("해당 채팅방에 존재하지 않는 사용자입니다."));
        initChatRoomInteract(chatRoom);
        return chatLogicHandler.chatLogic(chatRequest);
    }

    @Transactional
    public ChatRoomResponse createChatRoom(ChatRoomRequest chatRoomRequest, AccessToken accessToken) {
        String buyerStudentId = accessToken.studentId();

        UsedBook usedBook = usedBookRepository.findById(chatRoomRequest.getUsedBookId())
                .orElseThrow(() -> new ChatException("거래 글을 찾을 수 없습니다."));

        Member seller = usedBook.getSellerMember();
        Member buyer = memberRepository.findByStudentId(buyerStudentId)
                .orElseThrow(() -> new ChatException("사용자를 찾을 수 없습니다."));

        if (seller.getStudentId().equals(buyerStudentId)) {
            throw new ChatException("본인이 판매하는 전공서적은\n구매할 수 없어요!");
        }

        if (usedBook.getBookStatus().equals(BookStatus.TRADE)) {
            throw new ChatException("거래가 진행중인\n전공서적입니다!");
        } else if (usedBook.getBookStatus().equals(BookStatus.SOLD)) {
            throw new ChatException("거래가 완료된\n전공서적입니다!");
        }

        Optional<ChatRoom> result = isChatRoomAlreadyExistsBySellerBuyerUsedBook(usedBook, seller,
                buyer);
        if (result.isPresent()) {
            return result.map(chatRoom -> ChatRoomResponse.of(chatRoom.getChatRoomId())).orElse(null);
        }
        usedBook.setBuyerMember(buyer);

        ChatRoom savedChatroom = buildAndSaveChatRoom(usedBook, seller, buyer);

        ChatRequest chatRequest = ChatRequest.of(savedChatroom.getChatRoomId(), BOOK_PURCHASE_START);
        chatRequestHandler(chatRequest, accessToken);

        return ChatRoomResponse.of(savedChatroom.getChatRoomId());
    }

    private Optional<ChatRoom> isChatRoomAlreadyExistsBySellerBuyerUsedBook(UsedBook usedBook,
                                                                            Member seller, Member buyer) {
        Optional<ChatRoom> result = chatRoomRepository.findBookBySellerAndBuyerAndBook(seller,
                buyer, usedBook);

        return result;
    }

    private ChatRoom buildAndSaveChatRoom(UsedBook usedBook, Member seller, Member buyer) {
        ChatRoom chatRoom = buildChatRoom(buyer, seller, usedBook);
        ChatRoom savedChatroom = chatRoomRepository.save(chatRoom);
        return savedChatroom;
    }

    @Transactional
    public List<ChatMessageResponse> getChattingData(long chatRoomId, AccessToken accessToken) {

        String studentId = accessToken.studentId();

        boolean isMemberBuyer = chatRoomRepository.existsByChatRoomIdAndBuyer_StudentId(chatRoomId,
                studentId);

        if (!isMemberBuyer) {
            boolean isMemberSeller = chatRoomRepository.existsByChatRoomIdAndSeller_StudentId(chatRoomId,
                    studentId);

            if (!isMemberSeller) {
                throw new ChatException("채팅방에 속해있지 않습니다.");
            } else {
                chatRoomRepository.updateSellerChattingToRead(chatRoomId);
                return chatRoomRepository.findChatMessageByChatroomIdWithSellerStudentId(chatRoomId,
                        studentId);
            }
        } else {
            chatRoomRepository.updateBuyerChattingToRead(chatRoomId);
            return chatRoomRepository.findChatMessageByChatroomIdWithBuyerStudentId(chatRoomId,
                    studentId);
        }
    }

    @Transactional
    public List<ChatMessageResponse> getNotReadChattingData(long chatRoomId,
                                                            AccessToken accessToken) {
        ChatRoom chatRoom = findByChatRoomId(chatRoomId);

        if (chatRoom.getSeller().getStudentId().equals(accessToken.studentId())) {
            List<ChatMessageResponse> responseList = chatRoomRepository.getNotReadSellerChattingData(
                    chatRoomId);
            chatRoomRepository.modifySellerChattingToRead(chatRoomId);
            return responseList;
        }

        if (chatRoom.getBuyer().getStudentId().equals(accessToken.studentId())) {
            List<ChatMessageResponse> responseList = chatRoomRepository.getNotReadBuyerChattingData(
                    chatRoomId);
            chatRoomRepository.modifyBuyerChattingToRead(chatRoomId);
            return responseList;
        }

        throw new ChatException("해당 채팅방에 존재하지 않는 회원입니다.");
    }

    public List<ChatListResponse> getChatListWithAccessToken(AccessToken accessToken) {
        String studentId = jwtTokenUtils.getStudentId(accessToken.value());

        return chatRoomRepository.findByChatroomListWithStudentId(studentId);
    }

    @Transactional
    public List<ChatMessageResponseWithUsedBookId> stopTrade(ChatStopRequest chatStopRequest,
                                                             AccessToken accessToken) {
        String studentId = accessToken.studentId();

        ChatRoom chatRoom = findByChatRoomId(chatStopRequest.getChatRoomId());

        Member seller = chatRoomRepository.getSeller(chatStopRequest.getChatRoomId());
        Member buyer = chatRoomRepository.getBuyer(chatStopRequest.getChatRoomId());

        if (chatRoom.getInteractStep() > 100 || chatRoom.getInteractStep() == 6) {
            throw new ChatException("이미 거래가 종료되었습니다.");
        }
        if (chatRoom.getInteractStep() == 4 || chatRoom.getInteractStep() > 5) {
            releaseLocker(chatRoom);
        }

        chatRoom.setInteractStep(999);

        if (seller.getStudentId().equals(studentId)) {
            return chatBotAdmin.tradeStopBySeller(chatRoom, seller, buyer);
        } else if (buyer.getStudentId().equals(studentId)) {
            return chatBotAdmin.tradeStopByBuyer(chatRoom, seller, buyer);
        } else {
            throw new ChatException("해당 채팅방에 존재하지 않는 회원입니다.");
        }
    }

    public void releaseLocker(ChatRoom chatRoom) {
        Locker locker = lockerRepository.findByChatRoom(chatRoom)
                .orElseThrow(() -> new ChatException("해당 채팅방에 사물함이 없습니다."));

        locker.releaseLocker();
    }

    @Transactional
    public void deleteUsedBookByChatRoomIdWhenSellerCanceledChat(Long chatRoomId,
                                                                 AccessToken accessToken) {
        String studentId = accessToken.studentId();

        ChatRoom findChatRoom = findByChatRoomId(chatRoomId);

        if (findChatRoom.getInteractStep() < 999) {
            throw new ChatException("거래가 진행중인 채팅방은 삭제할 수 없습니다.");
        }

        if (findChatRoom.getBuyer().getStudentId().equals(studentId)) {
            throw new ChatException("판매자만 책을 삭제할 수 있습니다.");
        }

        findChatRoom.getUsedBook().setIsDeletedTrue();
    }
    private static void initChatRoomInteract(ChatRoom chatRoom) {
        chatRoom.setInteractStep(0);
    }

}
