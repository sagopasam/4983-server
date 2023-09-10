package team.dankookie.server4983;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import team.dankookie.server4983.jwt.constants.TokenSecretKey;

@EnableConfigurationProperties(TokenSecretKey.class)
@SpringBootApplication
public class Server4983Application {

    public static void main(String[] args) {
        SpringApplication.run(Server4983Application.class, args);
    }

}
