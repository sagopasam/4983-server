package team.dankookie.server4983.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.dankookie.server4983.jwt.domain.RefreshToken;
import team.dankookie.server4983.jwt.repository.RefreshTokenRepository;
import team.dankookie.server4983.member.domain.Member;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken save(Member member, RefreshToken refreshToken) {
        if (refreshTokenRepository.existsByMember(member)) {
            RefreshToken findRefreshToken = refreshTokenRepository.findByMember(member)
                    .orElseThrow(() -> new IllegalArgumentException("member에 해당하는 refreshToken이 없습니다."));
            findRefreshToken.updateRefreshToken(refreshToken.getRefreshToken());

            return findRefreshToken;
        }

        return refreshTokenRepository.save(refreshToken);
    }
}
