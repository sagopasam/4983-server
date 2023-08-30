package team.dankookie.server4983.member.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dankookie.server4983.common.BaseRepositoryTest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends BaseRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void 사용자의_nickname으로_멤버를_찾는다() {
        //given
        final String nickname = "nickname";

        Member member = MemberFixture.createMemberByNickname(nickname);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByNickname(nickname);

        //then
        assertThat(findMember).isPresent();
    }

    @Test
    void 사용자의_학번으로_사용자를_찾는다() {
        //given
        final String studentId = "studentId";
        Member member = MemberFixture.createMemberByStudentId(studentId);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByStudentId(studentId);

        //then
        assertThat(findMember).isPresent();
    }

}