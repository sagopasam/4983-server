package team.dankookie.server4983.member.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberMyPageModifyResponse;
import team.dankookie.server4983.member.dto.MemberMyPageResponse;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages/member")
public class MemberMyPageController {

  private final MemberService memberService;

  @GetMapping
  public ResponseEntity<MemberMyPageResponse> getMemberInfo(AccessToken accessToken) {
    MemberMyPageResponse response = memberService.getMyPageMemberInfo(
        accessToken.nickname());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/modify")
  public ResponseEntity<MemberMyPageModifyResponse> getMemberModifyInfo(AccessToken accessToken){
    MemberMyPageModifyResponse response = memberService.getMyPageMemberModifyInfo(
            accessToken.nickname());
    return ResponseEntity.ok(response);
  }

}
