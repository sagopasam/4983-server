package team.dankookie.server4983.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.fcm.dto.FcmBaseRequest;
import team.dankookie.server4983.fcm.dto.FcmTargetUserIdRequest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

  private final FirebaseMessaging firebaseMessaging;
  private final MemberService memberService;

  @Async("messagingTaskExecutor")
  public void sendNotificationByToken(FcmBaseRequest request) {

    Member member = memberService.findMemberById(request.targetUserId());

    if (member.getFirebaseToken() != null) {
      Notification notification = Notification.builder()
          .setTitle(request.title())
          .setBody(request.body())
          // .setImage(requestDto.getImage())
          .build();

      Message message = Message.builder()
          .setToken(member.getFirebaseToken())
          .setNotification(notification)
          // .putAllData(requestDto.getData())
          .build();

      try {
        firebaseMessaging.send(message);
      } catch (FirebaseMessagingException e) {
//        e.printStackTrace();
//        throw new RuntimeException("알림 보내기를 실패하였습니다. targetUserId=" + request.targetUserId());
        log.warn("알림 보내기를 실패하였습니다. targetUserId={}", request.targetUserId());
      }
    } else {
//                throw new RuntimeException("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId="
//                    + request.targetUserId());
      log.warn("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId={}"
          , request.targetUserId());
    }
  }

  @Async("messagingTaskExecutor")
  public void sendChattingNotificationByToken(FcmTargetUserIdRequest request) {
    FcmBaseRequest fcmBaseRequest = FcmBaseRequest.of(
        request.targetUserId(),
        //FIXME : title 명 수정 필요
        "채팅 알림",
        request.message()
    );

    sendNotificationByToken(fcmBaseRequest);
  }
}
