package team.dankookie.server4983.member.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberLogoutController {
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response, @CookieValue(name = "refreshToken", required = false) Cookie refreshTokenCookie) {
        if (refreshTokenCookie != null) {
            refreshTokenCookie.setMaxAge(0);
            response.addCookie(refreshTokenCookie);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
