package team.dankookie.server4983.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.common.BaseServiceTest;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.*;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.repository.memberImage.MemberImageRepository;
import team.dankookie.server4983.s3.dto.S3Response;
import team.dankookie.server4983.s3.service.S3UploadService;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest extends BaseServiceTest {

  @InjectMocks
  MemberService memberService;

  @Mock
  S3UploadService uploadService;

  @Mock
  MemberRepository memberRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  JwtTokenUtils jwtTokenUtils;

  @Mock
  TokenSecretKey tokenSecretKey;

  @Mock
  MemberImageRepository memberImageRepository;

  @Mock
  EntityManager em;


  @Test
  void 유저의_학번과_비밀번호가_일치하면_true를_리턴한다() {
    //given
    LoginRequest loginRequest = LoginRequest.of("studentId", "password");

    Member member = Member.builder().nickname("studentId").studentId("studentId")
        .password("password").isWithdraw(false).build();

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

    Member member = Member.builder().nickname("studentId").studentId("studentId")
        .password("password").build();

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

    Member member = Member.builder().nickname("studentId").studentId("studentId")
        .password("password").isWithdraw(false).build();

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
    Member member = Member.builder().nickname("studentId").build();

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
    Member member = Member.builder().nickname("studentId").build();

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
    boolean isMemberExists = memberService.isMemberExistsByMemberPasswordRequest(studentId,
        phoneNumber);
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
    assertThatThrownBy(
        () -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("존재하지 않는 학번입니다.");
  }

  @Test
  void 학번과_휴대폰번호가_일치하는_회원이_없으면_에러를_던진다() {
    //given
    String studentId = "studentId";
    String phoneNumber = "phoneNumber";
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");


    when(memberRepository.findByStudentId(accessToken.studentId()))
        .thenReturn(Optional.of(Member.builder().phoneNumber("wrongPhoneNumber").build()));
    //when
    //then
    assertThatThrownBy(
        () -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("학번과 맞지 않는 휴대폰번호입니다.");
  }

  @Test
  void 학번과_비밀번호_변경할_비밀번호를_받으면_비밀번호를_변경한다() {
    //given
    MemberTemporaryPasswordChangeRequest request = MemberTemporaryPasswordChangeRequest.of("studentId", "phoneNumber",
        "password");
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");

    Member member = Member.builder().nickname("studentId").studentId("studentId")
        .phoneNumber("phoneNumber").password("password").build();

    when(memberRepository.findByStudentId(accessToken.studentId()))
        .thenReturn(Optional.of(member));

    //when
    boolean isChanged = memberService.changeMemberTemporaryPassword(request);

    //then
    assertThat(isChanged).isTrue();

  }


  @Test
  void 비밀번호_변경_학번과_일치하는_회원이_없으면_에러를_던진다() {
    //given
    String studentId = "studentId";
    String phoneNumber = "phoneNumber";
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");

    when(memberRepository.findByStudentId(accessToken.studentId()))
        .thenThrow(new IllegalArgumentException("존재하지 않는 학번입니다."));
    //when
    //then
    assertThatThrownBy(
        () -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
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
    assertThatThrownBy(
        () -> memberService.isMemberExistsByMemberPasswordRequest(studentId, phoneNumber))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("학번과 맞지 않는 휴대폰번호입니다.");
  }

  @Test
  void 비밀번호_변경_닉네임과_일치하는_회원이_없으면_에러를_던진다() {
    //given
    String password = "mockedPassword";
    String studentId = "mockedStudentId";
    MemberPasswordRequest request = MemberPasswordRequest.of(password);

    when(memberRepository.findByStudentId(studentId))
        .thenThrow(new IllegalArgumentException("존재하지 않는 사용자입니다."));
    //when
    //then
    assertThatThrownBy(() ->
        memberService.isMemberPasswordMatch(request, studentId))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("존재하지 않는 사용자입니다.");
  }

  @Test
  void 현재_비밀번호와_작성한_비밀번호가_일치하면_true를_리턴한다() {
    //given
    String password = "mockedPassword";
    String studentId = "mockedStudentId";
    MemberPasswordRequest request = MemberPasswordRequest.of(password);

    Member findMember = Member.builder().studentId("studentId").build();
    when(memberRepository.findByStudentId(studentId))
        .thenReturn(Optional.of(findMember));
    when(passwordEncoder.matches(password, findMember.getPassword()))
        .thenReturn(true);
    //when
    boolean result = memberService.isMemberPasswordMatch(MemberPasswordRequest.of(request.password()), studentId);
    //then
    assertThat(result).isTrue();
  }

  @Test
  void 현재_비밀번호와_작성한_비밀번호가_다르면_false를_리턴한다() {
    //given
    String password = "mockedPassword";
    String studentId = "mockedStudentId";

    MemberPasswordRequest request = MemberPasswordRequest.of(password);
    Member findMember = Member.builder().studentId("studentId").build();
    when(memberRepository.findByStudentId(studentId))
        .thenReturn(Optional.of(findMember));
    when(passwordEncoder.matches(request.password(), findMember.getPassword()))
        .thenReturn(false);

    //when
    boolean result = memberService.isMemberPasswordMatch(MemberPasswordRequest.of(request.password()), studentId);

    //then
    assertThat(result).isFalse();
  }

  @Test
  void 회원이_탈퇴되었으면_isWithdraw를_true로_반환한다() {
    //given
    AccessToken accessToken = new AccessToken("dummy_access_token", "testNickname");

    Member findMember = Member.builder().studentId("studentId").isWithdraw(false).build();
    when(memberRepository.findByStudentId(accessToken.studentId()))
        .thenReturn(Optional.of(findMember));

    //when
    boolean isWithdraw = memberService.checkMemberAndWithdraw(accessToken);
    //then
    assertThat(isWithdraw).isTrue();
    assertThat(findMember.getIsWithdraw()).isTrue();
  }

  @Test
  void 프로필_전체_수정() {
    //given
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");
    MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg",
        MediaType.IMAGE_JPEG_VALUE, "test image body".getBytes());

    Member findMember = Member.builder()
        .id(1L)
        .studentId("studentId")
        .nickname("testNickname")
        .accountBank(AccountBank.K)
        .accountNumber("000000-00-000000")
        .imageUrl("https://4983-s3.s3.ap-northeast-2.amazonaws.com/test.png")
        .build();


    when(memberRepository.findByStudentId("studentId"))
        .thenReturn(Optional.of(findMember));

    S3Response s3Response = S3Response.of("test.jpg", "testKey", "http://example.com/test.jpg");
    when(uploadService.saveFileWithUUID(multipartFile)).thenReturn(s3Response);

    MemberProfileSaveRequest memberProfileSaveRequest = MemberProfileSaveRequest.of(
        "김민진",
        AccountBank.KB,
        "938002-00-613983",
        "01012341234"
    );
    //when
    MemberProfileSaveResponse response = memberService.updateMemberProfile(multipartFile,
        memberProfileSaveRequest, accessToken);
    //then
    assertEquals(findMember.getId(), response.memberId());
  }


  @Test
  void 프로필_이미지_외_나머지_수정() {
    //given
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");

    Member findMember = Member.builder()
        .id(1L)
        .studentId("studentId")
        .nickname("testNickname")
        .accountBank(AccountBank.K)
        .accountNumber("000000-00-000000")
        .build();

    when(memberRepository.findByStudentId("studentId"))
        .thenReturn(Optional.of(findMember));

    MemberProfileSaveRequest memberProfileSaveRequest = MemberProfileSaveRequest.of(
        "김민진",
        AccountBank.KB,
        "938002-00-613983",
        "01012341234"
    );
    //when
    MemberProfileSaveResponse response = memberService.updateMemberProfile(null,
        memberProfileSaveRequest, accessToken);
    //then
    assertEquals(findMember.getId(), response.memberId());
  }

  @Test
  void 변경할_비밀번호를_받으면_비밀번호를_변경한다() {
    //given
    String initialPassword = "password";
    String newPassword = "changePassword";
    AccessToken accessToken = new AccessToken("dummy_access_token", "studentId");

    Member findmember = Member.builder().studentId("studentId").password(initialPassword).build();
    MemberPasswordChangeRequest request = MemberPasswordChangeRequest.of(newPassword);

    when(memberRepository.findByStudentId(accessToken.studentId()))
            .thenReturn(Optional.of(findmember));
    //when
    boolean isChanged = memberService.changeMemberPassword(request, accessToken);
    //then
    assertThat(isChanged).isTrue();
    assertThat(passwordEncoder.matches(newPassword, findmember.getPassword()));
  }

}

@SpringBootTest
class SpringBootTestExample {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  void test() throws Exception {

    String password = "password";
    String encodedPassword = passwordEncoder.encode(password);

    boolean matches = passwordEncoder.matches(password, encodedPassword);

    assertThat(matches).isTrue();
  }
}

