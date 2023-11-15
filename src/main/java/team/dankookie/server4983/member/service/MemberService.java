package team.dankookie.server4983.member.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.*;
import team.dankookie.server4983.member.repository.MemberRepository;
import team.dankookie.server4983.member.repository.memberImage.MemberImageRepository;
import team.dankookie.server4983.s3.dto.S3Response;
import team.dankookie.server4983.s3.service.S3UploadService;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final S3UploadService uploadService;
  private final MemberRepository memberRepository;
  private final MemberImageRepository memberImageRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtils jwtTokenUtils;
  private final EntityManager entityManager;

  public Member findMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
  }

  public Member findMemberByStudentId(String studentId) {
    return memberRepository.findByStudentId(studentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

  }

  public boolean login(LoginRequest loginRequest) {

    Member member = memberRepository.findByStudentId(loginRequest.studentId())
        .orElseThrow(() -> new LoginFailedException("존재하지 않는 학번입니다."));

    if (member.getIsWithdraw()) {
      throw new LoginFailedException("탈퇴한 회원입니다.");
    }

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
  public boolean changeMemberPassword(MemberPasswordChangeRequest request, AccessToken accessToken) {
    entityManager.clear();
    Member member = memberRepository.findByStudentId(accessToken.studentId())
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

  @Transactional
  public boolean isMemberPasswordMatch(MemberPasswordRequest request, String studentId) {
    Member findMember = findMemberByStudentId(studentId);
    return passwordEncoder.matches(request.password(), findMember.getPassword());
  }

  public MemberCollegeAndDepartment findMemberCollegeAndDepartment(AccessToken accessToken) {
    String nickname = jwtTokenUtils.getStudentId(accessToken.value());
    Member member = findMemberByStudentId(nickname);

    return MemberCollegeAndDepartment.of(member.getDepartment());
  }

  @Transactional
  public boolean checkMemberAndWithdraw(AccessToken accessToken) {
    entityManager.clear();

    Member member = findMemberByStudentId(accessToken.studentId());
    if (!member.getIsWithdraw()) {
      member.withdraw();
    }
    return member.getIsWithdraw();
  }

  @Transactional
  public MemberProfileSaveResponse updateMemberProfile(MultipartFile multipartFile,
      MemberProfileSaveRequest memberProfileSaveRequest, AccessToken accessToken) {

    String studentId = accessToken.studentId();
    Member member = memberRepository.findByStudentId(studentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

    member.updateMemberProfile(memberProfileSaveRequest);

    if (multipartFile != null) {
      String imageUrl = member.getImageUrl();
      String s3BucketUrl = uploadService.getS3BucketUrl();
      String imageKey = imageUrl.split(s3BucketUrl)[1];

      uploadService.deleteFile(imageKey);

      S3Response s3Response = uploadService.saveFileWithUUID(multipartFile);
      member.setImageUrl(s3Response.s3ImageUrl());
    }

    return MemberProfileSaveResponse.of(member.getId());
  }

  public MemberMyPageResponse getMyPageMemberInfo(String studentId) {
    Member member = findMemberByStudentId(studentId);

    return MemberMyPageResponse.of(member.getImageUrl(), member.getNickname());
  }

  @Transactional
  public void deleteMyPageProfileImage(MemberImageRequest memberImageRequest, String studentId) {
    String imageUrl = memberImageRequest.imageUrl();
    String imageKey = imageUrl.split(uploadService.getS3BucketUrl())[1];

    uploadService.deleteFile(imageKey);

    final String BASE_IMAGE = "https://4983-s3.s3.ap-northeast-2.amazonaws.com/baseImage.png";

    Member member = findMemberByStudentId(studentId);
    member.setImageUrl(BASE_IMAGE);
  }

  public MemberMyPageModifyResponse getMyPageMemberModifyInfo(String studentId) {
    Member member = findMemberByStudentId(studentId);
    return MemberMyPageModifyResponse.of(member.getImageUrl(), member.getNickname(),
        member.getAccountBank(), member.getAccountNumber(), member.getPhoneNumber());
  }

  @Transactional
  public boolean checkPhoneNumberDuplicate(String phoneNumber, String studentId) {
    Member findMember = findMemberByStudentId(studentId);
    return findMember.getPhoneNumber().equals(phoneNumber)
           || memberRepository.existsMemberByPhoneNumber(phoneNumber);
  }
}

