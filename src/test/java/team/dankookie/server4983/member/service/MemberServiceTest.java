package team.dankookie.server4983.member.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.dankookie.server4983.common.BaseServiceTest;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.dto.MemberPasswordChangeRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


class MemberServiceTest extends BaseServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    PasswordEncoder passwordEncoder;


    @Test
    void 유저의_학번과_비밀번호가_일치하면_true를_리턴한다() {
        //given
        LoginRequest loginRequest = LoginRequest.of("studentId", "password");

        Member member = Member.builder().nickname("nickname").studentId("studentId").password("password").build();

        when(memberRepository.findByStudentId(loginRequest.studentId()))
                .thenReturn(Optional.of(member));

        when(passwordEncoder.matches(loginRequest.password(), member.getPassword()))
                .thenReturn(true);

        //when
        boolean login = memberService.login(loginRequest);

        //then
        assertThat(login).isEqualTo(true);
    }

    @Test
    void 학번이_일치하지않는_경우_에러를_던진다() {
        //given
        LoginRequest loginRequest = LoginRequest.of("studentId", "password");

        Member member = Member.builder().nickname("nickname").studentId("studentId").password("password").build();

        when(memberRepository.findByStudentId(loginRequest.studentId()))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("존재하지 않는 학번입니다.");

    }

    @Test
    void 학번은_존재하는데_비밀번호가_일치하지_않으면_에러를_던진다() {
        //given
        LoginRequest loginRequest = LoginRequest.of("studentId", "password");

        Member member = Member.builder().nickname("nickname").studentId("studentId").password("password").build();

        when(memberRepository.findByStudentId(loginRequest.studentId()))
                .thenReturn(Optional.of(member));

        when(passwordEncoder.matches(loginRequest.password(), member.getPassword()))
                .thenReturn(false);
        //when
        //then
        assertThatThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("잘못된 비밀번호입니다!");

    }

    @Test
    void 학번으로_유저의_닉네임을_알아낸다() {
        //given
        String studentId = "studentId";
        Member member = Member.builder().nickname("nickname").build();

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(member));
        //when
        Member findMember = memberService.findMemberNicknameByStudentId(studentId);

        //then
        assertThat(findMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void 학번이_존재하지_않으면_에러를_던진다() {
        //given
        String studentId = "studentId";
        Member member = Member.builder().nickname("nickname").build();

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> memberService.findMemberNicknameByStudentId(studentId))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("존재하지 않는 학번입니다.");
    }

    @Test
    void 학번과_휴대폰번호가_일치하는_회원이_있으면_true를_리턴한다() {
        //given
        String studentId = "studentId";
        String phoneNumber = "phoneNumber";

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(Member.builder().phoneNumber(phoneNumber).build()));
        //when
        boolean isMemberExists = memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber);
        //then
        assertThat(isMemberExists).isEqualTo(true);
    }

    @Test
    void 학번과_일치하는_회원이_없으면_에러를_던진다() {
        //given
        String studentId = "studentId";
        String phoneNumber = "phoneNumber";

        when(memberRepository.findByStudentId(studentId))
                .thenThrow(new IllegalArgumentException("존재하지 않는 학번입니다."));
        //when
        //then
        assertThatThrownBy(() -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 학번입니다.");
    }

    @Test
    void 학번과_휴대폰번호가_일치하는_회원이_없으면_에러를_던진다() {
        //given
        String studentId = "studentId";
        String phoneNumber = "phoneNumber";

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(Member.builder().phoneNumber("wrongPhoneNumber").build()));
        //when
        //then
        assertThatThrownBy(() -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학번과 맞지 않는 휴대폰번호입니다.");
    }

    @Test
    void 학번과_비밀번호_변경할_비밀번호를_받으면_비밀번호를_변경한다() {
        //given
        MemberPasswordChangeRequest request = MemberPasswordChangeRequest.of("studentId", "phoneNumber", "password");

        Member member = Member.builder().nickname("nickname").studentId("studentId").phoneNumber("phoneNumber").password("password").build();

        when(memberRepository.findByStudentId(request.studentId()))
                .thenReturn(Optional.of(member));

        //when
        boolean isChanged = memberService.changeMemberPassword(request);

        //then
        assertThat(isChanged).isTrue();

    }


    @Test
    void 비밀번호_변경_학번과_일치하는_회원이_없으면_에러를_던진다() {
        //given
        String studentId = "studentId";
        String phoneNumber = "phoneNumber";

        when(memberRepository.findByStudentId(studentId))
                .thenThrow(new IllegalArgumentException("존재하지 않는 학번입니다."));
        //when
        //then
        assertThatThrownBy(() -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 학번입니다.");
    }

    @Test
    void 비밀번호_변경_학번과_휴대폰번호가_일치하는_회원이_없으면_에러를_던진다() {
        //given
        String studentId = "studentId";
        String phoneNumber = "phoneNumber";

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(Member.builder().phoneNumber("wrongPhoneNumber").build()));
        //when
        //then
        assertThatThrownBy(() -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("학번과 맞지 않는 휴대폰번호입니다.");
    }

    @Test
    void 비밀번호_변경_닉네임과_일치하는_회원이_없으면_에러를_던진다() {
        //given
        String accessToken = "mockedAccessToken";
        String password = "mockedPassword";
        String nickname = "mockedNickname";


        try (MockedStatic<JwtTokenUtils> mockedStatic = Mockito.mockStatic(JwtTokenUtils.class)) {
            mockedStatic.when(() -> JwtTokenUtils.getNickname(any(), any())).thenReturn(nickname);

            when(memberRepository.findByNickname(nickname))
                    .thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));
            //when
            //then
            assertThatThrownBy(() ->
                    memberService.isMemberPasswordMatch(password, accessToken))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("존재하지 않는 사용자입니다.");

        }
    }

    @Test
    void 현재_비밀번호와_작성한_비밀번호가_일치하면_true를_리턴한다() {
        //given
        String accessToken = "mockedAccessToken";
        String password = "mockedPassword";
        String nickname = "mockedNickname";

        // Mock JwtTokenUtils.getNickname() 메서드 호출
        try (MockedStatic<JwtTokenUtils> mockedStatic = Mockito.mockStatic(JwtTokenUtils.class)) {
            mockedStatic.when(() -> JwtTokenUtils.getNickname(Mockito.any(), Mockito.any())).thenReturn(nickname);


            Member findMember = Member.builder().nickname("nickname").build();
            when(memberRepository.findByNickname(nickname))
                    .thenReturn(Optional.of(findMember));
            when(passwordEncoder.matches(password, findMember.getPassword()))
                    .thenReturn(true);
            //when
            boolean result = memberService.isMemberPasswordMatch(password, accessToken);
            //then
            assertThat(result).isTrue();
        }
    }

    @Test
    void 현재_비밀번호와_작성한_비밀번호가_다르면_false를_리턴한다() {
        //given
        String accessToken = "mockedAccessToken";
        String password = "mockedPassword";
        String nickname = "mockedNickname";

        try (MockedStatic<JwtTokenUtils> mockedStatic = Mockito.mockStatic(JwtTokenUtils.class)) {
            mockedStatic.when(() -> JwtTokenUtils.getNickname(Mockito.any(), Mockito.any())).thenReturn(nickname);

            Member findMember = Member.builder().nickname("nickname").build();
            when(memberRepository.findByNickname(nickname))
                    .thenReturn(Optional.of(findMember));
            when(passwordEncoder.matches(password, findMember.getPassword()))
                    .thenReturn(false);

            //when
            boolean result = memberService.isMemberPasswordMatch(password, accessToken);

            //then
            assertFalse(result);
        }
    }

    }

