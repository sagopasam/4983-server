package team.dankookie.server4983.chat.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;

import java.util.Map;

import static team.dankookie.server4983.chat.constant.ContentType.*;

@Component
@RequiredArgsConstructor
public class ChatLogicHandler {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void chatLoginHandler(ChatRequest chatRequest, String userName) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        Member seller = chatRoomRepository.getSeller(chatRequest.getChatRoomId());

        switch (chatRequest.getContentType()) {
            case BOOK_PURCHASE_START: // SELLCHAT_1_1 판매자 구매 요청
                purchaseBookStart(chatRoom, userName, seller.getNickname());
                break;
            case BOOK_PURCHASE_REQUEST: // SELLCHAT_2 판매자 구매 수락
                purchaseBookApprove(chatRoom, seller.getNickname());
                break;
            case BOOK_SALE_REJECTION: // SELLCHAT_1_2 거래 거절
                rejectPurchaseRequest(chatRoom , userName, seller.getNickname());
                break;
            case PAYMENT_CONFIRMATION_COMPLETE: // SELLCHAT_4 입금 확인
                confirmDeposit(chatRoom);
                break;
            case BOOK_PLACEMENT_COMPLETE:
                completeSelectLockAndPassword(chatRoom , chatRequest);
                break;
            case TRADE_COMPLETE:
                completeTrade(chatRoom);
                break;
            default:
                throw new ChatException("잘못된 데이터 요청입니다.");
        }
    }

    public void purchaseBookStart(ChatRoom chatRoom, String userName, String sellerNickName) {
        String sellerMessage = String.format("\'%s\' 님이 거래 요청을 보냈어요! \n" +
                "오늘 거래하러 갈래요?", userName);
        String buyerMessage = String.format("\'%s\' 님께 \'%s\' 서적 거래를 요청했습니다. \n\n" +
                " 판매자의 응답을 기다려주세요. :)", sellerNickName, chatRoom.getUsedBook().getName());

        chatRoom.addSellerChat(SellerChat.buildSellerChat(sellerMessage, BOOK_PURCHASE_START));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(buyerMessage, BOOK_PURCHASE_START));
    }

    public void purchaseBookApprove(ChatRoom chatRoom, String sellerNickName) {
        String sellerMessage = String.format("구매자에게 요청 수락 알림을 보냈습니다. \n" +
                "입금이 확인될 때 까지 기다려주세요. :)");
        String buyerMessage = String.format("\'%s\' 님께 \'%s\' 서적 거래를 요청을 수락했습니다. \n" +
                "아래 계좌정보로 결제금액을 송금하여 주십시오. \n" +
                "\n계좌번호:\n" +
                "우리 은행 1002-3597-18283 (사고팔삼)\n" +
                "결제 금액 : %d원", sellerNickName, chatRoom.getUsedBook().getName(), chatRoom.getUsedBook().getPrice());

        chatRoom.addSellerChat(SellerChat.buildSellerChat(sellerMessage, BOOK_PURCHASE_REQUEST));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(buyerMessage, BOOK_PURCHASE_REQUEST));
    }

    public void rejectPurchaseRequest(ChatRoom chatRoom, String userName, String sellerNickName) {
        String sellerMessage = String.format("‘%s’ 님의 ‘%s’ 서적 거래 요청을 거절하셨습니다.\n" +
                "이후에도, 해당 서적 판매를 이어나가길 원하신다면, ‘네'를 클릭하여 거래 날짜를 수정해주세요.\n" +
                "\n" +
                "‘아니오’ 선택 시, 24시간 이내 해당 판매 게시글이 삭제됩니다,", userName, chatRoom.getUsedBook().getName());
        String buyerMessage = String.format("아쉽게도 '%s' 님과의 서적 거래가 \n" +
                "이루어지지 못했습니다. \n", sellerNickName);

        chatRoom.addSellerChat(SellerChat.buildSellerChat(sellerMessage, BOOK_SALE_REJECTION));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(buyerMessage, BOOK_SALE_REJECTION));
    }

    public void confirmDeposit(ChatRoom chatRoom) {
        String sellerMessage = String.format("입금이 확인되었습니다. \n" +
                "구매자가 “거래 완료\" 버튼을 클릭 후 \n" +
                "판매 금액이 자동으로 입금될 예정입니다.\n" +
                "\n" +
                "책을 넣을 사물함을 선택하고 \n" +
                "비밀번호를 설정해주세요. \n");
        String buyerMessage = String.format("입금이 확인되었습니다.\n" +
                "\n" +
                "거래날짜에 판매자가 사물함에 서적을 \n" +
                "배치할 예정입니다.\n");

        chatRoom.addSellerChat(SellerChat.buildSellerChat(sellerMessage, PAYMENT_CONFIRMATION_COMPLETE));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(buyerMessage, PAYMENT_CONFIRMATION_COMPLETE));
    }

    public void completeSelectLockAndPassword(ChatRoom chatRoom , ChatRequest request) {
        String sellerMessage = String.format("서적 배치가 완료되었습니다.\n" +
                "금일 내 수령해주시길 바랍니다.\n" +
                "\n" +
                "“거래 완료\" 버튼을 눌러야, 판매자에게 판매금액이 입금되오니, 수령 후 버튼을 꼭 눌러주세요 \n" +
                "\n" +
                "사물함 번호: %s번\n" +
                "사물함 비밀번호: %s\n" , request.getData().get("lockerNumber") , request.getData().get("lockerPassworda") );
        String buyerMessage = String.format("기입하셨던 거래 날짜에 맞게, 당일 내에 배치 해주시길 바랍니다. \n" +
                "\n" +
                "서적 배치 이후 완료 버튼을 눌러주세요.\n" +
                "\n" +
                "구매자가 배치된 서적을 수령한 후, “거래 완료” 버튼을 클릭하면 판매금액이 자동으로 입금됩니다.\n" +
                "\n");

        chatRoom.addSellerChat(SellerChat.buildSellerChat(sellerMessage, BOOK_PLACEMENT_COMPLETE));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(buyerMessage, BOOK_PLACEMENT_COMPLETE));
    }

    public void completeTrade(ChatRoom chatRoom) {
        String message = String.format("거래가 완료되었습니다.\n" +
                "이용해주셔서 감사합니다.\n" +
                "\n" +
                "-사고파삼-\n");

        chatRoom.addSellerChat(SellerChat.buildSellerChat(message, TRADE_COMPLETE));
        chatRoom.addBuyerChat(BuyerChat.buildBuyerChat(message, TRADE_COMPLETE));
    }

}
