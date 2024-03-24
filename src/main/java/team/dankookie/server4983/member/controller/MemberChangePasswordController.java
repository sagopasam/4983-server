package team.dankookie.server4983.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberPasswordChangeRequest;
import team.dankookie.server4983.member.dto.MemberPasswordMatchResponse;
import team.dankookie.server4983.member.dto.MemberPasswordRequest;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages")
public class MemberChangePasswordController {
    private final MemberService memberService;

    @PostMapping("/change-password/verify-current-password")
    public ResponseEntity<MemberPasswordMatchResponse> getCurrentPassword(@RequestBody final MemberPasswordRequest request, AccessToken accessToken){

        boolean isPasswordMatch = memberService.isMemberPasswordMatch(request, accessToken.studentId());
        return ResponseEntity.ok(MemberPasswordMatchResponse.of(isPasswordMatch));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody MemberPasswordChangeRequest request, AccessToken accessToken) {
        boolean isChanged = memberService.changeMemberPassword(request, accessToken);
        return ResponseEntity.ok().build();
    }


}
