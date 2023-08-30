package team.dankookie.server4983.member.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import team.dankookie.server4983.common.BaseServiceTest;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
                .isInstanceOf(LoginFailedException.class);

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
                .isInstanceOf(LoginFailedException.class);

    }

    @Test
    void 학번으로_유저의_닉네임을_알아낸다() {
        //given
        String studentId = "studentId";
        Member member = Member.builder().nickname("nickname").build();

        when(memberRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(member));
        //when
        String nickname = memberService.findMemberNicknameByStudentId(studentId);

        //then
        assertThat(nickname).isEqualTo(member.getNickname());
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
                .isInstanceOf(LoginFailedException.class);
    }
    

}