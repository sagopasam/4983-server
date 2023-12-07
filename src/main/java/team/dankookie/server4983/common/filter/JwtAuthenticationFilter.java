package team.dankookie.server4983.common.filter;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import team.dankookie.server4983.common.exception.NotAuthorizedException;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;
import team.dankookie.server4983.member.service.MemberDetailsService;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtils jwtTokenUtils;
  private final MemberDetailsService memberDetailsService;

  private final List<String> EXCLUDE_URI = List.of(
      "/docs/**",
      "/api/v1/token/valid",
      "/api/v1/token/update",
      "/api/v1/login",
      "/api/v1/members/password/certification-number",
      "/api/v1/members/password",
      "/api/v1/register/duplicate/studentId",
      "/api/v1/register/duplicate/nickname",
      "/api/v1/my-pages/certification-number",
      "/api/v1/register",
      "/api/v1/admin/login"
  );

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    log.info("requestURI: {}", requestURI);

    if (EXCLUDE_URI.stream().anyMatch(requestURI::startsWith)) {
      log.info("exclude uri: {}", requestURI);
      filterChain.doFilter(request, response);
      return;
    }

    if (request.getHeader(AUTHORIZATION) == null || request.getHeader(AUTHORIZATION).isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = request.getHeader(AUTHORIZATION);

    String studentId = jwtTokenUtils.getStudentId(accessToken);

    Boolean isTokenValid = jwtTokenUtils.validate(accessToken, studentId);

    if (!isTokenValid) {
      throw new NotAuthorizedException("유효하지 않은 토큰입니다.");
    }

    Boolean isTokenExpired = jwtTokenUtils.isTokenExpired(accessToken);
    if (isTokenExpired) {
      throw new NotAuthorizedException("만료된 토큰입니다.");
    }

    UserDetails member = memberDetailsService.loadUserByUsername(studentId);

    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        member,
        null,
        member.getAuthorities()
    );

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);

    SecurityContextHolder.setContext(securityContext);

    filterChain.doFilter(request, response);
  }

  private String parseBearerToken(HttpServletRequest request) {

    String accessToken = request.getHeader(AUTHORIZATION);

    if (StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ")) {
      return accessToken.substring(7);
    } else {
      throw new NotAuthorizedException("잘못된 토큰입니다.");
    }
  }
}
