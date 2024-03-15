package team.dankookie.server4983.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.fcm.dto.FcmBaseRequest;
import team.dankookie.server4983.fcm.dto.FcmChatRequest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@Service
public class FcmService {

    private final FirebaseMessaging firebaseMessaging;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Transactional
    public void updateFcmToken(String studentId, String token) {

        Member findMember = memberRepository.findByStudentId(studentId)
                .orElseThrow(
                        () -> new IllegalArgumentException("해당하는 학번의 유저가 존재하지 않습니다.")
                );

        findMember.updateToken(token);
        memberRepository.save(findMember);
    }

    @Async("messagingTaskExecutor")
    public void sendNotificationByToken(FcmBaseRequest request) {

        Member member = memberService.findMemberById(request.targetUserId());

        if (member.getFirebaseToken() != null) {
            Notification notification = Notification.builder()
                    .setTitle(request.title())
                    .setBody(request.body())
                    .setImage("https://4983-s3.s3.ap-northeast-2.amazonaws.com/appIcon.png")
                    .build();

            Message message = Message.builder()
                    .setToken(member.getFirebaseToken())
                    .setNotification(notification)
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                log.warn("알림 보내기를 실패하였습니다. targetUserId={}", request.targetUserId());
            }
        } else {
            log.warn("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId={}"
                    , request.targetUserId());
        }
    }

    @Async("messagingTaskExecutor")
    public void sendChattingNotificationByToken(FcmChatRequest request) {
        final Member member = memberService.findMemberById(request.targetUserId());

        if (member.existFirebaseToken()) {
            Notification notification = Notification.builder()
                    .setTitle("사고파삼")
                    .setBody(request.message())
                    .setImage("https://4983-s3.s3.ap-northeast-2.amazonaws.com/appIcon.png")
                    .build();

            Message message = Message.builder()
                    .setToken(member.getFirebaseToken())
                    .setNotification(notification)
                    .putData("screenName", request.screenName())
                    .putData("chatRoomId", request.chatRoomId().toString())
                    .build();

            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException e) {
                log.error("알림 보내기를 실패하였습니다. targetUserId={}", request.targetUserId());
            }
            return;
        }
        log.error("서버에 저장된 해당 유저의 FirebaseToken이 존재하지 않습니다. targetUserId={}", request.targetUserId());
    }
}
