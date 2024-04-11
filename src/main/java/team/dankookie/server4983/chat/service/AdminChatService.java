package team.dankookie.server4983.chat.service;

import static team.dankookie.server4983.chat.constant.ContentType.ADMIN_REQUEST_MESSAGE_TO_BUYER;
import static team.dankookie.server4983.chat.constant.ContentType.ADMIN_REQUEST_MESSAGE_TO_SELLER;
import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PLACEMENT_COMPLETE;
import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PLACEMENT_SET;
import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PURCHASE_REQUEST;
import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PURCHASE_START;
import static team.dankookie.server4983.chat.constant.ContentType.BOOK_SALE_REJECTION;
import static team.dankookie.server4983.chat.constant.ContentType.CANCEL;
import static team.dankookie.server4983.chat.constant.ContentType.PAYMENT_CONFIRMATION_COMPLETE;
import static team.dankookie.server4983.chat.constant.ContentType.TRADE_COMPLETE;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.AdminChatMessageResponse;
import team.dankookie.server4983.chat.dto.AdminChatRoomListResponse;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.handler.ChatBotAdmin;
import team.dankookie.server4983.chat.handler.ChatLogicHandler;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatLogicHandler chatLogicHandler;
    private final ChatBotAdmin chatBotAdmin;
    private final LockerRepository lockerRepository;


    public Page<AdminChatRoomListResponse> getChatList(Pageable pageable, String searchKeyword,
                                                       int interact) {
        return chatRoomRepository.getAdminChatList(pageable, searchKeyword, interact);
    }

    @Transactional
    public void updateInteract(Long chatRoomId, int interact) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방이 없습니다. id=" + chatRoomId));

        switch (interact) {
            case 1 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, BOOK_PURCHASE_START));
            }
            case 2 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, BOOK_PURCHASE_REQUEST));
            }
            case 3 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, PAYMENT_CONFIRMATION_COMPLETE));
            }
            case 4 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, BOOK_PLACEMENT_SET));
            }
            case 5 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, BOOK_PLACEMENT_COMPLETE));
            }
            case 6 -> {
                initChatRoomInteract(chatRoom);
                ifChatRoomEmptySaveMockLocker(chatRoom);

                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, TRADE_COMPLETE));
            }
            case 999 -> {
                initChatRoomInteract(chatRoom);
                chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, BOOK_SALE_REJECTION));
            }
            case 1000 -> chatBotAdmin.tradeStopBySeller(
                    chatRoom,
                    chatRoomRepository.getSeller(chatRoomId),
                    chatRoomRepository.getBuyer(chatRoomId)
            );
            case 1001 -> {
                chatRoomRepository.getBuyer(chatRoomId);
                chatBotAdmin.tradeStopByBuyer(
                        chatRoom,
                        chatRoomRepository.getSeller(chatRoomId),
                        chatRoomRepository.getBuyer(chatRoomId)
                );
            }
        }
    }

    public List<AdminChatMessageResponse> getBuyerChat(Long chatRoomId) {
        return chatRoomRepository.findBuyerChatListByChatRoomId(chatRoomId);
    }

    public List<AdminChatMessageResponse> getSellerChat(Long chatRoomId) {
        return chatRoomRepository.findSellerChatListByChatRoomId(chatRoomId);
    }

    @Transactional
    public void cancel(final Long chatRoomId) {
        chatRoomRepository.findById(chatRoomId)
                .ifPresent(chatRoom -> chatLogicHandler.chatLogic(ChatRequest.of(chatRoomId, CANCEL)));
    }

    @Transactional
    public void postChat(Long chatRoomId, String message, ContentType contentType) {

        switch (contentType) {
            case ADMIN_REQUEST_MESSAGE_TO_BUYER -> {
                ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                        .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
                BuyerChat buyerChat = BuyerChat.buildBuyerChat(message, ADMIN_REQUEST_MESSAGE_TO_BUYER,
                        chatRoom);
                chatRoom.addBuyerChat(buyerChat);
            }
            case ADMIN_REQUEST_MESSAGE_TO_SELLER -> {
                ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRoomId)
                        .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));
                SellerChat sellerChat = SellerChat.buildSellerChat(message, ADMIN_REQUEST_MESSAGE_TO_SELLER,
                        chatRoom);
                chatRoom.addSellerChat(sellerChat);
            }
        }

    }

    private void ifChatRoomEmptySaveMockLocker(ChatRoom chatRoom) {
        if (lockerRepository.findByChatRoom(chatRoom).isEmpty()) {
            lockerRepository.save(
                    Locker.builder()
                            .chatRoom(chatRoom)
                            .isExists(false)
                            .lockerNumber(0)
                            .password("1234")
                            .tradeDate(LocalDate.now())
                            .build()
            );
        }
    }

    private static void initChatRoomInteract(ChatRoom chatRoom) {
        chatRoom.setInteractStep(0);
    }
}
