package team.dankookie.server4983.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtils {

    public Boolean validate(String token, String nickname, String key) {
        String nicknameByToken = getNickname(token, key);
        return nicknameByToken.equals(nickname) && !isTokenExpired(token, key);
    }

    public Claims extractAllClaims(String token, String key) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getNickname(String token, String key) {
        return extractAllClaims(token, key).get("nickname", String.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    public String generateJwtToken(String nickname, String key, long expiredTimeMs) {
        return doGenerateToken(nickname, expiredTimeMs, key);
    }

    private String doGenerateToken(String nickname, long expireTime, String key) {
        Claims claims = Jwts.claims();
        claims.put("nickname", nickname);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

}
