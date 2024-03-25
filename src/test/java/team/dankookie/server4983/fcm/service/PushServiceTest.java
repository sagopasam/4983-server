package team.dankookie.server4983.fcm.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.fcm.dto.AdminPushRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

@Service
@ExtendWith(MockitoExtension.class)
public class PushServiceTest {

    @InjectMocks
    PushService pushService;

    @Mock
    FcmService fcmService;

    @Mock
    MemberRepository memberRepository;

    @Test
    void isALL이_참_이면_모든_회원에게_알림을_보낸다() {
        // given
        AdminPushRequest pushRequest = new AdminPushRequest(null, List.of("1", "2"), null, null, true);

        // when
        pushService.push(pushRequest);

        // then
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void isAll이_참이_아니면_조건에_따른_회원에_대해서만_알림을_보낸다() {
        // given
        AdminPushRequest pushRequest = new AdminPushRequest(null, List.of("1", "2"), null, null, false);

        // when
        pushService.push(pushRequest);

        // then
        verify(memberRepository, times(1)).findByPushCondition(List.of("1", "2"), null, null);
    }
}

