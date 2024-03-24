package team.dankookie.server4983.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret-key}")
    private String key;

    public Boolean validate(String token, String studentId) {
        String studentIdByToken = getStudentId(token);
        return studentIdByToken.equals(studentId) && !isTokenExpired(token);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(key))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getStudentId(String token) {
        return extractAllClaims(token).get("studentId", String.class);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String generateJwtToken(String studentId, long expiredTimeMs) {
        return doGenerateToken(studentId, expiredTimeMs, key);
    }

    private String doGenerateToken(String studentId, long expireTime, String key) {
        Claims claims = Jwts.claims();
        claims.put("studentId", studentId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(key), SignatureAlgorithm.HS256)
                .compact();
    }

}
