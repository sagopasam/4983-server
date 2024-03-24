package team.dankookie.server4983.fcm.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import team.dankookie.server4983.fcm.dto.AdminPushRequest;
import team.dankookie.server4983.fcm.dto.FcmBaseRequest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushService {

    private final FcmService fcmService;
    private final MemberRepository memberRepository;
    private final String DEFAULT_TITLE = "사고파삼";

    public void push(AdminPushRequest request) {
        if (request.isAll()) {
            pushAllMembers(request);
            return;
        }

        final List<Member> members = memberRepository.findByPushCondition(request.getMemberIds(), request.getDepartments(), request.getYearOfAdmissions());
        final FcmBaseRequest fcmBaseRequest = FcmBaseRequest.of(members, DEFAULT_TITLE, request.getMessage());
        fcmService.sendNotificationByToken(fcmBaseRequest);
    }

    private void pushAllMembers(final AdminPushRequest request) {
        final List<Member> members = memberRepository.findAll();
        final FcmBaseRequest fcmBaseRequest = FcmBaseRequest.of(members, DEFAULT_TITLE, request.getMessage());
        fcmService.sendNotificationByToken(fcmBaseRequest);
    }
}
