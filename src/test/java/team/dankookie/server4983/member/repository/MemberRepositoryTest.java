package team.dankookie.server4983.member.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dankookie.server4983.common.BaseRepositoryTest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.domain.MemberImage;
import team.dankookie.server4983.member.fixture.MemberFixture;
import team.dankookie.server4983.member.repository.memberImage.MemberImageRepository;

class MemberRepositoryTest extends BaseRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberImageRepository memberImageRepository;

    @Test
    void 어드민페이지의_알림_발송_조건에_맞춰_멤버를_찾는다() {

        //given
        String studentId = "studentId";
        Member member1 = MemberFixture.createMemberByNickname(studentId);
        memberRepository.save(member1);
        studentId = "studentId2";
        Member member2 = MemberFixture.createMemberByNickname(studentId);
        memberRepository.save(member2);

        //when
        List<Member> findMembers = memberRepository.findByPushCondition(List.of(member1.getId().toString(),member2.getId().toString()),
                List.of(member1.getDepartment(),member2.getDepartment()),
                List.of(member1.getYearOfAdmission(),member2.getYearOfAdmission()));

        //then
        assertThat(findMembers.size()).isEqualTo(2);
    }

    @Test
    void 사용자의_nickname으로_멤버를_찾는다() {
        //given
        final String studentId = "studentId";

        Member member = MemberFixture.createMemberByNickname(studentId);
        memberRepository.save(member);

        //when
        Optional<Member> findMember = memberRepository.findByStudentId(studentId);

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

    @Test
    void 닉네임이_중복인지_확인한다() {
        //given
        final String nickname = "studentId";

        Member member = MemberFixture.createMemberByNickname(nickname);
        memberRepository.save(member);

        //when
        boolean duplicateNickname = memberRepository.existsMemberByNickname(nickname);
        //then
        assertThat(duplicateNickname).isTrue();
    }

    @Test
    void 프로필_이미지를_삭제한다() {

        //given
        final String nickname = "studentId";

        Member member = MemberFixture.createMemberByNickname(nickname);
        memberRepository.save(member);
        MemberImage memberImage = MemberImage.builder()
                .member(member)
                .imageUrl(member.getImageUrl()).build();
        memberImageRepository.save(memberImage);
        memberImageRepository.deleteByMember(member);
    }
}