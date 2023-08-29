package team.dankookie.server4983.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.jwt.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
