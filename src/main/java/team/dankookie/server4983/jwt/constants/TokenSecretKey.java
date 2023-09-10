package team.dankookie.server4983.jwt.constants;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class TokenSecretKey {

    private String secretKey;
}
