package team.dankookie.server4983.common;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import team.dankookie.server4983.config.TestConfig;

@DataJpaTest
@Import(TestConfig.class)
public class BaseRepositoryTest extends BaseDisplayNameConfig {
}
