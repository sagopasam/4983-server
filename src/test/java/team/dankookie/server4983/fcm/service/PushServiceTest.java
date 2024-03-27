package team.dankookie.server4983.fcm.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.fcm.dto.AdminPushRequest;
import team.dankookie.server4983.fcm.dto.FcmBaseRequest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;
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

    @DisplayName("isAll 분기 테스트")
    @Nested
    class isAllTest {
        @Test
        @DisplayName("isAll이 true 일 때")
        void isALL이_참_이면_모든_회원에게_알림을_보낸다() {

            // given
            String studentId = "studentId";
            AdminPushRequest pushRequest = new AdminPushRequest(null, null,
                    null, "test", true);
            List<Member> memberList = new ArrayList<>();
            memberList.add(MemberFixture.createMemberByStudentIdAndNickname(studentId, "testNickname"));
            memberList.add(MemberFixture.createMemberByStudentIdAndNickname(studentId, "testNickname1"));

            // when
            pushService.push(pushRequest);

            // then
            verify(memberRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("isAll이 false 일 때")
        void isAll이_참이_아니면_조건에_따른_회원에_대해서만_알림을_보낸다() {
            // given
            AdminPushRequest pushRequest = new AdminPushRequest(List.of(Department.ARCHITECTURE, Department.SOFTWARE),
                    List.of("1", "2"), List.of(2023, 2022), "test", false);
            List<Member> memberList = new ArrayList<>();
            memberList.add(Member.builder().id(1L).department(Department.ARCHITECTURE).yearOfAdmission(2022).firebaseToken("123").build());
            memberList.add(Member.builder().id(2L).department(Department.SOFTWARE).yearOfAdmission(2023).firebaseToken("456").build());

            // when
            pushService.push(pushRequest);
            ArgumentCaptor<FcmBaseRequest> captor = ArgumentCaptor.forClass(FcmBaseRequest.class);


            // then
            verify(fcmService, times(1)).sendNotificationByToken(captor.capture());

            List<Member> notifiedMembers = captor.getValue().members();
            // then
            assertEquals(notifiedMembers.size(),2);
        }
    }

}

