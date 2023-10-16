package team.dankookie.server4983.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.chat.domain.BuyerChat;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.chat.domain.SellerChat;
import team.dankookie.server4983.chat.repository.BuyerChatRepository;
import team.dankookie.server4983.chat.repository.SellerChatRepository;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.scheduler.constant.ScheduleType;
import team.dankookie.server4983.scheduler.entity.Schedule;
import team.dankookie.server4983.scheduler.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.util.List;

import static team.dankookie.server4983.chat.constant.ContentType.TRADE_WARNING_BUYER;
import static team.dankookie.server4983.chat.constant.ContentType.TRADE_WARNING_SELLER;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final FcmService fcmService;
    private final LockerRepository lockerRepository;
    private final BuyerChatRepository buyerChatRepository;
    private final SellerChatRepository sellerChatRepository;

//    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void execute() {
        List<Schedule> result = schedulerRepository.findByAlertTime(LocalDateTime.now());

        for (Schedule schedule : result) {
            fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(schedule.getTargetId(), schedule.getMessage()));
            saveChatInteract(schedule);
            schedulerRepository.deleteById(schedule.getId());
        }
    }

    public void saveChatInteract(Schedule schedule) {
        ScheduleType scheduleType = schedule.getScheduleType();
        ChatRoom chatRoom = schedule.getChatRoom();

        switch (scheduleType) {
            case BUYER_CASE_1:
            case BUYER_CASE_2:
                BuyerChat buyerChat = BuyerChat.buildBuyerChat(schedule.getMessage(), TRADE_WARNING_BUYER, chatRoom);

                chatRoom.addBuyerChat(buyerChat);
                buyerChatRepository.save(buyerChat);
                break;
            case SELLER_CASE_2:
            case SELLER_CASE_3:
                SellerChat sellerChat = SellerChat.buildSellerChat(schedule.getMessage(), TRADE_WARNING_SELLER, chatRoom);

                chatRoom.addSellerChat(sellerChat);
                sellerChatRepository.save(sellerChat);
                break;
            default:
                break;
        }
    }

    // 판매자
    @Transactional
    public void setSchedulerAboutNotReply(ChatRoom chatRoom) {
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(2);

        Schedule schedule = Schedule.builder().targetId(chatRoom.getSeller().getId()).chatRoom(chatRoom).time(localDateTime).scheduleType(ScheduleType.SELLER_CASE_2)
                .message("아직 사물함 설정이 완료되지 않았어요!\n " +
                        "\"거래하러 가기\" 버튼을 클릭하여 사물함 번호와 비밀번호를 꼭 선택해 주세요!")
                .build();

        schedulerRepository.save(schedule);
    }

    @Transactional
    public void setSchedulerAboutNotComplete(ChatRoom chatRoom, LocalDateTime tradeAvailableDate , Locker locker) {
        LocalDateTime time = tradeAvailableDate.withHour(8).withMinute(30);

        Schedule schedule = Schedule.builder().targetId(chatRoom.getSeller().getId()).chatRoom(chatRoom).time(time).scheduleType(ScheduleType.SELLER_CASE_3)
                .message(String.format("오늘은 거래 하는 날이에요!\n" +
                        "수업 가는 길에 전공 책을 꼭 챙겨주세요!\n" +
                        "\n" +
                        "사물함은 상경관 2층 GS25 편의점 옆에 초록색 사물함을 찾아주세요:) \n" +
                        "\n" +
                        "사물함 번호 : %s번 \n " +
                        "거래 약속 시간: %d:%d \n",
                locker.getLockerNumber(), chatRoom.getUsedBook().getTradeAvailableDatetime().getHour() , chatRoom.getUsedBook().getTradeAvailableDatetime().getMinute()))
                .build();

        schedulerRepository.save(schedule);
    }

    @Transactional
    public void setSchedulerAboutSetPlacement(ChatRoom chatRoom) {
        LocalDateTime time = LocalDateTime.now().plusHours(2L);

        Schedule schedule = Schedule.builder().targetId(chatRoom.getSeller().getId()).chatRoom(chatRoom).time(time).scheduleType(ScheduleType.SELLER_CASE_2)
                .message(String.format("아직 사물함 설정이 완료되지 않았어요!\n" +
                        "“거래하러 가기\" 버튼을 클릭하여 사물함 번호와 비밀번호를 꼭 선택해 주세요!"))
                .build();

        schedulerRepository.save(schedule);
    }

    // 구매자
    @Transactional
    public void setSchedulerAboutNotDeposit(ChatRoom chatRoom, LocalDateTime tradeAvailableDate) {
        LocalDateTime time = tradeAvailableDate.minusDays(2);

        Schedule schedule = Schedule.builder().targetId(chatRoom.getBuyer().getId()).chatRoom(chatRoom).time(time).scheduleType(ScheduleType.BUYER_CASE_1)
                .message(String.format("아직 입금 확인이 안되었어요! \n" +
                                "입금이 완료되어야, 판매자가 사물함에 서적을 배치할 수 있어요! "))
                .build();

        schedulerRepository.save(schedule);
    }

    @Transactional
    public void setSchedulerAboutDontPressDone(ChatRoom chatRoom, LocalDateTime tradeAvailableDate) {
        Locker locker = lockerRepository.findByChatRoom(chatRoom)
            .orElseThrow(() -> new IllegalArgumentException("해당 채팅방에 사물함이 없습니다."));
        LocalDateTime time = tradeAvailableDate.withHour(8).withMinute(30);

        Schedule schedule = Schedule.builder().targetId(chatRoom.getBuyer().getId()).chatRoom(chatRoom).time(time).scheduleType(ScheduleType.BUYER_CASE_2)
                .message(String.format("오늘은 거래 하는 날이에요!\n" +
                                "수업 가는 길에 전공 책을 꼭 수령해주세요!\n" +
                                "\n" +
                                "사물함은 상경관 2층 GS25 옆에 초록색 사물함을 찾아주세요:) \n" +
                                "\n" +
                                "사물함 번호 : %s번 \n " +
                                "거래 약속 시간: %d:%d \n",
                        locker.getLockerNumber(), chatRoom.getUsedBook().getTradeAvailableDatetime().getHour() , chatRoom.getUsedBook().getTradeAvailableDatetime().getMinute()))
                .build();

        schedulerRepository.save(schedule);
    }
}
