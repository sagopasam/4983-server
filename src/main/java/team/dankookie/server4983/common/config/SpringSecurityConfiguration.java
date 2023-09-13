package team.dankookie.server4983.common.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import team.dankookie.server4983.jwt.repository.RefreshTokenRepository;
import team.dankookie.server4983.member.service.MemberService;

//FIXME : authorizeHttpRequest 추후 수정 필요
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final MemberService memberService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> req
                        .anyRequest().permitAll())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }


}
