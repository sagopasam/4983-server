package team.dankookie.server4983.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberMyPageModifyResponse;
import team.dankookie.server4983.member.dto.MemberMyPageResponse;
import team.dankookie.server4983.member.dto.MemberPasswordMatchResponse;
import team.dankookie.server4983.member.dto.MemberPasswordRequest;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages/member")
public class MemberMyPageController {

  private final MemberService memberService;

  @GetMapping
  public ResponseEntity<MemberMyPageResponse> getMemberInfo(AccessToken accessToken) {
    MemberMyPageResponse response = memberService.getMyPageMemberInfo(
        accessToken.studentId());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/password")
  public ResponseEntity<MemberPasswordMatchResponse> checkPasswordMatch(
      @RequestBody MemberPasswordRequest request, AccessToken accessToken) {
    boolean isPasswordMatch = memberService.isMemberPasswordMatch(request, accessToken.studentId());
    return ResponseEntity.ok(MemberPasswordMatchResponse.of(isPasswordMatch));
  }

  @GetMapping("/modify")
  public ResponseEntity<MemberMyPageModifyResponse> getMemberModifyInfo(AccessToken accessToken) {
    MemberMyPageModifyResponse response = memberService.getMyPageMemberModifyInfo(
        accessToken.studentId());
    return ResponseEntity.ok(response);
  }

}
