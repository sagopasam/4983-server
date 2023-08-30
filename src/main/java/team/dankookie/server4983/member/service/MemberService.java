package team.dankookie.server4983.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
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

    public String findMemberNicknameByStudentId(String studentId) {
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));
        return member.getNickname();
    }

}
