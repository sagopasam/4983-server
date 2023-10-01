package team.dankookie.server4983.scheduler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.book.domain.Locker;
import team.dankookie.server4983.book.repository.locker.LockerRepository;
import team.dankookie.server4983.chat.domain.ChatRoom;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.fcm.service.FcmService;
import team.dankookie.server4983.scheduler.constant.ScheduleType;
import team.dankookie.server4983.scheduler.entity.Schedule;
import team.dankookie.server4983.scheduler.repository.SchedulerRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SchedulerRepository schedulerRepository;
    private final FcmService fcmService;
    private final LockerRepository lockerRepository;

    @Scheduled(cron = "0 0 13 * * *") // 매일 오후 1시
    public void execute() {
        List<Schedule> result = schedulerRepository.findByAlertTime(LocalDateTime.now());

        for (Schedule schedule : result) {
            fcmService.sendChattingNotificationByToken(FcmTargetUserIdRequest.of(schedule.getTargetId(), schedule.getMessage()));
        }
    }

    // 판매자
    @Transactional
    public void setSchedulerAboutNotReply(ChatRoom chatRoom, LocalDate tradeAvailableDate) {
        tradeAvailableDate.minusDays(2);
        LocalDateTime time = LocalDateTime.of(tradeAvailableDate , LocalTime.of(13 , 0));

        Schedule schedule = Schedule.builder().chatRoom(chatRoom).time(time).scheduleType(ScheduleType.SELLER_CASE_2)
                .message("아직 사물함 설정이 완료되지 않았어요!\n " +
                        "\"거래하러 가기\" 버튼을 클릭하여 사물함 번호와 비밀번호를 꼭 선택해 주세요!")
                .build();

        schedulerRepository.save(schedule);
    }

    @Transactional
    public void setSchedulerAboutNotComplete(ChatRoom chatRoom, LocalDate tradeAvailableDate) {
        LocalDateTime time = LocalDateTime.of(tradeAvailableDate , LocalTime.of(8 , 30));

        Schedule schedule = Schedule.builder().chatRoom(chatRoom).time(time).scheduleType(ScheduleType.SELLER_CASE_3)
                .message(String.format("오늘은 거래 하는 날이에요!\n" +
                        "수업 가는 길에 전공 책을 꼭 챙겨주세요!\n" +
                        "\n" +
                        "사물함은 상경관 2층 GS25 편의점 옆에 초록색 사물함을 찾아주세요:) \n"))
                .build();

        schedulerRepository.save(schedule);
    }

    // 구매자
    @Transactional
    public void setSchedulerAboutNotDeposit(ChatRoom chatRoom, LocalDate tradeAvailableDate) {
        LocalDateTime time = LocalDateTime.of(tradeAvailableDate , LocalTime.now().withMinute(0));

        Schedule schedule = Schedule.builder().chatRoom(chatRoom).time(time).scheduleType(ScheduleType.BUYER_CASE_1)
                .message(String.format("아직 입금 확인이 안되었어요! \n" +
                                "입금이 완료되어야, 판매자가 사물함에 서적을 배치할 수 있어요! "))
                .build();

        schedulerRepository.save(schedule);
    }

    @Transactional
    public void setSchedulerAboutDontPressDone(ChatRoom chatRoom, LocalDate tradeAvailableDate) {
        Locker locker = lockerRepository.findByChatRoom(chatRoom);
        LocalDateTime time = LocalDateTime.of(tradeAvailableDate , LocalTime.of(8 , 30));

        Schedule schedule = Schedule.builder().chatRoom(chatRoom).time(time).scheduleType(ScheduleType.BUYER_CASE_2)
                .message(String.format("오늘은 거래 하는 날이에요!\n" +
                                "수업 가는 길에 전공 책을 꼭 수령해주세요!\n" +
                                "\n" +
                                "사물함은 상경관 2층 GS25 옆에 초록색 사물함을 찾아주세요:) \n" +
                                "\n" +
                                "사물함 번호 : %s번 \n " +
                                "거래 약속 시간: %d:%d \n",
                        locker.getLockerNumber(), locker.getTradeDate().getHour() , locker.getTradeDate().getMinute()))
                .build();

        schedulerRepository.save(schedule);
    }

}
