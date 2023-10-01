package team.dankookie.server4983.chat.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.BuyerChatRepository;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.chat.repository.SellerChatRepository;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.scheduler.repository.SchedulerRepository;
import team.dankookie.server4983.scheduler.service.SchedulerService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static team.dankookie.server4983.chat.constant.ContentType.*;
import static team.dankookie.server4983.scheduler.constant.ScheduleType.*;

@Component
@RequiredArgsConstructor
public class ChatLogicHandler {

    private final ChatRoomRepository chatRoomRepository;
    private final FcmService fcmService;
    private final LockerRepository lockerRepository;
    private final SchedulerService schedulerService;
    private final SchedulerRepository schedulerRepository;
    private final ChatBotInteract chatBotInteract;


    @Transactional
    public void chatLoginHandler(ChatRequest chatRequest, Member buyer) {
        ChatRoom chatRoom = chatRoomRepository.findByChatRoomId(chatRequest.getChatRoomId())
                .orElseThrow(() -> new ChatException("채팅방을 찾을 수 없습니다."));

        Member seller = chatRoomRepository.getSeller(chatRequest.getChatRoomId());

        switch (chatRequest.getContentType()) {
            case BOOK_PURCHASE_START: // SELLCHAT_1_1 판매자 구매 요청
                String startSellerMessage = chatBotInteract.purchaseBookStart(chatRoom);
                chatBotInteract.purchaseBookWarning(chatRoom);

                schedulerService.setSchedulerAboutNotReply(chatRoom , chatRoom.getUsedBook().getTradeAvailableDate());
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), startSellerMessage));
                break;
            case BOOK_PURCHASE_REQUEST: // SELLCHAT_2 판매자 구매 수락
                String requestBuyerMessage = chatBotInteract.purchaseBookApprove(chatRoom, seller.getNickname());
                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_2);

                schedulerService.setSchedulerAboutNotDeposit(chatRoom , chatRoom.getUsedBook().getTradeAvailableDate());
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), requestBuyerMessage));
                break;
            case BOOK_SALE_REJECTION: // SELLCHAT_1_2 거래 거절
                String bookSakeRejectionBuyerMessage = chatBotInteract.rejectPurchaseRequest(chatRoom, buyer.getNickname(), seller.getNickname());
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), bookSakeRejectionBuyerMessage));
                break;
            case PAYMENT_CONFIRMATION_COMPLETE: // SELLCHAT_4 입금 확인
                List<String> paymentConfirmationCompleteMessageList = chatBotInteract.confirmDeposit(chatRoom);

                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , BUYER_CASE_1);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), paymentConfirmationCompleteMessageList.get(0)));
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), paymentConfirmationCompleteMessageList.get(1)));
                break;
            case BOOK_PLACEMENT_SET : // 서적 위치 설정 완료
                Locker locker = saveLockerData(chatRequest , chatRoom);
                String bookPlacementCompleteBuyerMessage = chatBotInteract.completeSelectLockAndPassword(chatRoom, chatRequest , locker);

                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_2);
                schedulerService.setSchedulerAboutNotComplete(chatRoom , chatRoom.getUsedBook().getTradeAvailableDate() , locker);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), bookPlacementCompleteBuyerMessage));
                break;
            case BOOK_PLACEMENT_COMPLETE: // SELLCHAT_5 서적 배치 완료
                schedulerService.setSchedulerAboutDontPressDone(chatRoom , chatRoom.getUsedBook().getTradeAvailableDate());
                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_3);
                break;
            case TRADE_COMPLETE: // SELLCHAT_6 거래 완료
                String tradeCompleteSellerMessage = chatBotInteract.completeTrade(chatRoom);

                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , BUYER_CASE_2);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), tradeCompleteSellerMessage));
                break;
            default:
                throw new ChatException("잘못된 데이터 요청입니다.");
        }
    }

    public Locker saveLockerData(ChatRequest chatRequest , ChatRoom chatRoom) {
        if(lockerRepository.findByLockerNumber(chatRequest.getData().get("lockerNumber").toString()).isPresent()) {
            throw new ChatException("이미 사용중인 Locker 입니다.");
        }

        String lockerNumber = chatRequest.getData().get("lockerNumber").toString();
        String lockerPassword = chatRequest.getData().get("lockerPassword").toString();

        Locker locker = Locker.builder()
                .lockerNumber(lockerNumber)
                .password(lockerPassword)
                .chatRoom(chatRoom)
                .isExists(true)
                .tradeDate(LocalDateTime.now())
                .build();

        return lockerRepository.save(locker);
    }

}
