package team.dankookie.server4983.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.dto.MemberPasswordChangeRequest;
import team.dankookie.server4983.member.dto.MemberRegisterRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member loadUserByUsername(String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

    }

    public boolean login(LoginRequest loginRequest) {

        Member member = memberRepository.findByStudentId(loginRequest.studentId())
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));

        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new LoginFailedException("잘못된 비밀번호입니다!");
        }
        return true;
    }

    public Member findMemberNicknameByStudentId(String studentId) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));
        return member;
    }

    public boolean isMemberExistsByMemberPasswordRequest(String studentId, String phoneNumber) {

        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학번입니다."));

        if (!member.getPhoneNumber().equals(phoneNumber)) {
            throw new IllegalArgumentException("학번과 맞지 않는 휴대폰번호입니다.");
        }

        return true;
    }

    @Transactional
    public boolean changeMemberPassword(MemberPasswordChangeRequest request) {
        Member member = memberRepository.findByStudentId(request.getStudentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 학번입니다.")
                );

        if (!member.getPhoneNumber().equals(request.getPhoneNumber())) {
            throw new IllegalArgumentException("학번과 맞지 않는 휴대폰번호입니다.");
        }

        member.changePassword(passwordEncoder.encode(request.getPassword()));
        return true;
    }

    public boolean isStudentIdDuplicate(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    @Transactional
    public Member register(MemberRegisterRequest request) {
        return memberRepository.save(request.toEntity());
    }

}
