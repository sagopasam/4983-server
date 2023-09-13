package team.dankookie.server4983.chat.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;

import java.util.Map;

import static team.dankookie.server4983.chat.constant.ContentType.BOOK_PURCHASE_START;

@Component
@RequiredArgsConstructor
public class ChatLogicHandler {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public String chatLoginHandler(ChatRequest chatRequest , String userName) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        switch(chatRequest.getContentType()) {
            case BOOK_PURCHASE_START:
                return purchaseBookStart(chatRoom , userName);
            case BOOK_PURCHASE_REQUEST:

            case BOOK_SALE_REJECTION:

            case BOOK_SALE_AGREEMENT:

            case PAYMENT_CONFIRMATION_COMPLETE:

            case LOCKER_SELECTION_COMPLETE:

            case BOOK_PLACEMENT_COMPLETE:

            case TRADE_COMPLETE:

            default:
                return "잘못된 데이터 요청입니다.";
        }
    }

    public String purchaseBookStart(ChatRoom chatRoom , String userName) {
        String message = String.format("\'%s\' 님이 거래 요청을 보냈어요!\n오늘 거래하러 갈래요?" , userName);

        chatRoom.addSellerChat(SellerChat.buildSellerChat(message , BOOK_PURCHASE_START));
        return message;
    }

}
