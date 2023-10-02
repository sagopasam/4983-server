package team.dankookie.server4983.chat.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.exception.ChatException;
import team.dankookie.server4983.chat.repository.ChatRoomRepository;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.scheduler.repository.SchedulerRepository;
import team.dankookie.server4983.scheduler.service.SchedulerService;

import java.time.LocalDateTime;
import java.util.List;

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
                if(chatRoom.getInteractStep() >= 1) {
                    return;
                }
                String startSellerMessage = chatBotInteract.purchaseBookStart(chatRoom);
                chatBotInteract.purchaseBookWarning(chatRoom);

                schedulerService.setSchedulerAboutNotReply(chatRoom);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), startSellerMessage));
                break;
            case BOOK_PURCHASE_REQUEST: // SELLCHAT_2 판매자 구매 수락
                if(chatRoom.getInteractStep() >= 2) {
                    return;
                }
                String requestBuyerMessage = chatBotInteract.purchaseBookApprove(chatRoom, seller.getNickname());
                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_2);

                schedulerService.setSchedulerAboutSetPlacement(chatRoom);
                schedulerService.setSchedulerAboutNotDeposit(chatRoom , chatRoom.getUsedBook().getTradeAvailableDatetime());
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), requestBuyerMessage));
                break;
            case BOOK_SALE_REJECTION: // SELLCHAT_1_2 거래 거절
                if(chatRoom.getInteractStep() >= 100) {
                    return;
                }
                String bookSakeRejectionBuyerMessage = chatBotInteract.rejectPurchaseRequest(chatRoom, buyer.getNickname(), seller.getNickname());

                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), bookSakeRejectionBuyerMessage));
                break;
            case PAYMENT_CONFIRMATION_COMPLETE: // SELLCHAT_4 입금 확인
                if(chatRoom.getInteractStep() >= 3) {
                    return;
                }
                List<String> paymentConfirmationCompleteMessageList = chatBotInteract.confirmDeposit(chatRoom);

                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , BUYER_CASE_1);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), paymentConfirmationCompleteMessageList.get(0)));
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), paymentConfirmationCompleteMessageList.get(1)));
                break;
            case BOOK_PLACEMENT_SET : // 서적 위치 설정 완료
                if(chatRoom.getInteractStep() >= 4) {
                    return;
                }
                Locker locker = saveLockerData(chatRequest , chatRoom);
                String bookPlacementCompleteBuyerMessage = chatBotInteract.completeSelectLockAndPassword(chatRoom, chatRequest , locker);

                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_2);
                schedulerService.setSchedulerAboutNotComplete(chatRoom , chatRoom.getUsedBook().getTradeAvailableDatetime() , locker);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(buyer.getId(), bookPlacementCompleteBuyerMessage));
                break;
            case BOOK_PLACEMENT_COMPLETE: // SELLCHAT_5 서적 배치 완료
                schedulerService.setSchedulerAboutDontPressDone(chatRoom , chatRoom.getUsedBook().getTradeAvailableDatetime());
                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , SELLER_CASE_3);
                break;
            case TRADE_COMPLETE: // SELLCHAT_6 거래 완료
                if(chatRoom.getInteractStep() >= 5) {
                    return;
                }
                String tradeCompleteSellerMessage = chatBotInteract.completeTrade(chatRoom);

                releaseLocker(chatRoom);
                schedulerRepository.deleteByChatRoomAndScheduleType(chatRoom , BUYER_CASE_2);
                fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(seller.getId(), tradeCompleteSellerMessage));
                break;
            default:
                throw new ChatException("잘못된 데이터 요청입니다.");
        }
    }

    public Locker saveLockerData(ChatRequest chatRequest , ChatRoom chatRoom) {
        String lockerNumber = chatRequest.getData().get("lockerNumber").toString();
        String lockerPassword = chatRequest.getData().get("lockerPassword").toString();

        Locker result = lockerRepository.findByLockerNumberAndIsExists(lockerNumber);

        if(result != null) {
            System.out.println(result.getTradeDate().toString());
            System.out.println(chatRoom.getUsedBook().getTradeAvailableDatetime().toString());
        }
        if(result != null && result.getTradeDate().equals(chatRoom.getUsedBook().getTradeAvailableDatetime())) {
            throw new ChatException("이미 사용중인 Locker 입니다.");
        }

        Locker locker = Locker.builder()
                .lockerNumber(lockerNumber)
                .password(lockerPassword)
                .chatRoom(chatRoom)
                .isExists(true)
                .tradeDate(chatRoom.getUsedBook().getTradeAvailableDatetime())
                .build();

        return lockerRepository.save(locker);
    }

    public void releaseLocker(ChatRoom chatRoom) {
        Locker locker = lockerRepository.findByChatRoom(chatRoom);

        locker.releaseLocker();
    }

}
