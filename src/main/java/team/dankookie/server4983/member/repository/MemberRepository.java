package team.dankookie.server4983.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
