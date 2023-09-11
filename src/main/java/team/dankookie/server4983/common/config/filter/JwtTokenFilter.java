package team.dankookie.server4983.common.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import team.dankookie.server4983.jwt.constants.TokenDuration;
import team.dankookie.server4983.jwt.domain.RefreshToken;
import team.dankookie.server4983.jwt.repository.RefreshTokenRepository;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.service.MemberService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final String key;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Authorization header is null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();

            if (JwtTokenUtils.isTokenExpired(token, key)) {
                handleExpiredToken(request, response, filterChain);
                return;
            }

            setAuthentication(request, token);
        } catch (RuntimeException e) {
            log.error("Error occurred while filtering JWT token", e);
            filterChain.doFilter(request, response);
        }

        filterChain.doFilter(request, response);
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        Cookie refreshTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .findFirst()
                .orElse(null);

        if (refreshTokenCookie == null) {
            log.warn("Refresh token cookie is missing");
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = refreshTokenCookie.getValue().split(" ")[1].trim();

        if (JwtTokenUtils.isTokenExpired(refreshToken, key)) {
            log.warn("Refresh token is expired");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<RefreshToken> refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken);

        if (refreshTokenEntity.isEmpty()) {
            log.warn("Refresh token does not exist");
            filterChain.doFilter(request, response);
            return;
        }

        String generateAccessToken = JwtTokenUtils.generateJwtToken(refreshTokenEntity.get().getMember().getNickname(), key, TokenDuration.REFRESH_TOKEN_DURATION.getDuration());

        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + generateAccessToken);

        setAuthentication(request, generateAccessToken);
    }

    private void setAuthentication(HttpServletRequest request, String accessToken) {
        String username = JwtTokenUtils.getNickname(accessToken, key);
        Member member = memberService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                member, null, List.of(new SimpleGrantedAuthority(member.getRole().toString()))
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
