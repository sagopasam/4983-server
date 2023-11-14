package team.dankookie.server4983.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.dto.MemberPasswordMatchResponse;
import team.dankookie.server4983.member.service.MemberService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/my-pages")
public class MemberChangePasswordController {
    private final MemberService memberService;

    @PostMapping("/change-password/verify-current-password")
    public ResponseEntity<MemberPasswordMatchResponse> getCurrentPassword(@RequestBody final String password, AccessToken accessToken){

        boolean isPasswordMatch = memberService.isMemberPasswordMatch(password, accessToken.nickname());
        return ResponseEntity.ok(MemberPasswordMatchResponse.of(isPasswordMatch));
    }


}
