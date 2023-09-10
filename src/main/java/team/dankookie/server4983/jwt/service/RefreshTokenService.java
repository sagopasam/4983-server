package team.dankookie.server4983.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.dankookie.server4983.jwt.domain.RefreshToken;
import team.dankookie.server4983.jwt.repository.RefreshTokenRepository;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }
}
