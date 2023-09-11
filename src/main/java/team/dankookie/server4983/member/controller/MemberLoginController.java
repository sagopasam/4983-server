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
import team.dankookie.server4983.jwt.domain.RefreshToken;
import team.dankookie.server4983.jwt.service.RefreshTokenService;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.LoginRequest;
import team.dankookie.server4983.member.service.MemberService;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static team.dankookie.server4983.jwt.constants.TokenDuration.ACCESS_TOKEN_DURATION;
import static team.dankookie.server4983.jwt.constants.TokenDuration.REFRESH_TOKEN_DURATION;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberLoginController {

    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @PostMapping("/login")
    public ResponseEntity<Void> login(HttpServletResponse response, @RequestBody LoginRequest loginRequest) {
        boolean isMemberExists = memberService.login(loginRequest);

        if (isMemberExists) {
            Member member =  memberService.findMemberNicknameByStudentId(loginRequest.studentId());

            setAccessTokenToHeader(response, member);
            setRefreshTokenToCookie(response, member);

            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
    }

    private void setAccessTokenToHeader(HttpServletResponse response, Member member) {
        String accessToken = JwtTokenUtils.generateJwtToken(member.getNickname(), secretKey, ACCESS_TOKEN_DURATION.getDuration());
        response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
    }

    private void setRefreshTokenToCookie(HttpServletResponse response, Member member) {
        String refreshToken = JwtTokenUtils.generateJwtToken(member.getNickname(), secretKey, REFRESH_TOKEN_DURATION.getDuration());
        refreshTokenService.save(
                RefreshToken.builder()
                        .member(member)
                        .refreshToken(refreshToken)
                        .build()
        );
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        response.addCookie(refreshTokenCookie);
    }
}
