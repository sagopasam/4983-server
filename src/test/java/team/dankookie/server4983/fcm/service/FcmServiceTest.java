package team.dankookie.server4983.fcm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.fcm.dto.FcmBaseRequest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;

@Service
@ExtendWith(MockitoExtension.class)
public class FcmServiceTest {

    @InjectMocks
    FcmService fcmService;

    @Mock
    FirebaseMessaging firebaseMessaging;

    @Test
    void 푸시_알림_테스트() throws FirebaseMessagingException {

        Member member = MemberFixture.createMemberByFirebaseToken(
                "c0wh23o5GE6YsajT1VvxZe:APA91bGnxR_1bFfFHs99doT5jBvvO1QF_g35QaVmFsVuR8ESAkPe_k-c4izefdtwkS6RgyZo2EYwEAB_EhgkKs6Yd3tXJTJxtcSTyzDXpcciEHDwtajhZ6aitbumT5jzIltb2wxPvSnQ");
        fcmService.sendNotificationByToken(FcmBaseRequest.of(List.of(member), "test", "test"));
        verify(firebaseMessaging, times(1)).send(any(Message.class));

    }
}

