package team.dankookie.server4983.member.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.dto.MemberCollegeAndDepartment;
import team.dankookie.server4983.member.dto.MemberPasswordChangeRequest;
import team.dankookie.server4983.member.dto.MemberRegisterRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtils jwtTokenUtils;
    private final TokenSecretKey tokenSecretKey;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Member testMember1 = Member.builder()
                .studentId("202023604")
                .department(Department.COMPUTER)
                .yearOfAdmission(2020)
                .nickname("닉네임1")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01073352306")
                .accountHolder("박재완")
                .accountBank(AccountBank.KB)
                .accountNumber("12500104097324")
                .marketingAgree(true)
                .build();

        Member testMember2 = Member.builder()
                .studentId("test")
                .department(Department.COMPUTER)
                .yearOfAdmission(2020)
                .nickname("닉네임2")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01073352306")
                .accountHolder("박박박")
                .accountBank(AccountBank.KB)
                .accountNumber("12500104097324")
                .marketingAgree(true)
                .build();


        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public Member findMemberByNickname(String nickname) {
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
        Member member = memberRepository.findByStudentId(request.studentId())
                .orElseThrow(
                        () -> new IllegalArgumentException("존재하지 않는 학번입니다.")
                );

        if (!member.getPhoneNumber().equals(request.phoneNumber())) {
            throw new IllegalArgumentException("학번과 맞지 않는 휴대폰번호입니다.");
        }

        member.changePassword(passwordEncoder.encode(request.password()));
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

        String encodedPassword = passwordEncoder.encode(request.password());

        return memberRepository.save(request.toEntity(encodedPassword));
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        return memberRepository.existsMemberByNickname(nickname);
    }

    public boolean isMemberPasswordMatch(String password, String accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken, secretKey);

        Member findMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (passwordEncoder.matches(password, findMember.getPassword())) {
            return true;
        }else {
            return false;
        }
    }

    public MemberCollegeAndDepartment findMemberCollegeAndDepartment(AccessToken accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken.value(), tokenSecretKey.getSecretKey());
        Member member = findMemberByNickname(nickname);

        return MemberCollegeAndDepartment.of(member.getDepartment());
    }
}
