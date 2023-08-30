package team.dankookie.server4983.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.service.MemberService;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static team.dankookie.server4983.jwt.constants.TokenDuration.ACCESS_TOKEN_DURATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberLoginController {

    private final MemberService memberService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        boolean isMemberExists = memberService.login(loginRequest);

        if (isMemberExists) {
            String nickname = memberService.findMemberNicknameByStudentId(loginRequest.studentId());

            response.setHeader(HttpHeaders.AUTHORIZATION, JwtTokenUtils.generateAccessToken(nickname, secretKey, ACCESS_TOKEN_DURATION.getDuration()));

            Cookie refreshTokenCookie = new Cookie("refreshToken", "refreshToken");
            response.addCookie(refreshTokenCookie);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
    }
}
