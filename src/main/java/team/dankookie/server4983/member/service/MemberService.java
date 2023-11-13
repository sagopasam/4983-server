package team.dankookie.server4983.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.common.exception.LoginFailedException;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.domain.MemberImage;
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
        String nickname = jwtTokenUtils.getNickname(accessToken);

        Member findMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return passwordEncoder.matches(password, findMember.getPassword());
    }

    public MemberCollegeAndDepartment findMemberCollegeAndDepartment(AccessToken accessToken) {
        String nickname = jwtTokenUtils.getNickname(accessToken.value());
        Member member = findMemberByNickname(nickname);

        return MemberCollegeAndDepartment.of(member.getDepartment());
    }

    @Transactional
    public boolean checkMemberAndWithdraw(AccessToken accessToken){
        String nickname = jwtTokenUtils.getNickname(accessToken.value());
        Member member = findMemberByNickname(nickname);
        if (!member.getIsWithdraw()) {
            member.withdraw();
        }
        return member.getIsWithdraw();
    }

    @Transactional
    public MemberProfileSaveResponse updateMemberProfile(MultipartFile multipartFile, MemberProfileSaveRequest memberProfileSaveRequest, AccessToken accessToken) {

        String nickname = accessToken.nickname();
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));


        member.updateMemberProfile(memberProfileSaveRequest);

        if (multipartFile != null) {
            memberImageRepository.deleteByMember(member);
                S3Response s3Response = uploadService.saveFileWithUUID(multipartFile);
                MemberImage memberImage = MemberImage.builder()
                        .member(member)
                        .imageUrl(s3Response.s3ImageUrl()).build();
                memberImageRepository.save(memberImage);
            }
        return MemberProfileSaveResponse.of(member.getId());
        }

    public MemberMyPageResponse getMyPageMemberInfo(String nickname) {
        Member member = findMemberByNickname(nickname);

        return MemberMyPageResponse.of(member.getImageUrl(), member.getNickname());
    }

    @Transactional
    public boolean deleteMyPageProfileImage(String image, String nickname) {
        Member member = findMemberByNickname(nickname);

        String imageUrl = uploadService.s3Bucket + image;

        long deleteCount = memberImageRepository.deleteMemberImageByMemberAndImageUrl(member, imageUrl);
        if (deleteCount == 0) {
            return false;
        }

        uploadService.deleteFile(image);
        return true;
    }
}

