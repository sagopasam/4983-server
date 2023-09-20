package team.dankookie.server4983.chat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.book.repository.usedBook.UsedBookRepository;
import team.dankookie.server4983.common.BaseRepositoryTest;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.fixture.MemberFixture;
import team.dankookie.server4983.member.repository.MemberRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ChatRoomRepositoryTest extends BaseRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    UsedBookRepository usedBookRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Test
    void 유저의_채팅_리스트를_리턴한다() {
        //given
        String nickname = "testNickname";
        Member member = MemberFixture.createMemberByNickname(nickname);
        Member otherMember = MemberFixture.createMember();
        memberRepository.save(member);
        memberRepository.save(otherMember);

        UsedBook usedBook1 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("사회과학통계방법")
                .buyerMember(otherMember)
                .sellerMember(member)
                .build();
        usedBookRepository.save(usedBook1);

        UsedBook usedBook2 = UsedBook.builder()
                .bookStatus(BookStatus.SALE)
                .name("사회과학통계방법")
                .buyerMember(member)
                .sellerMember(otherMember)
                .build();
        usedBookRepository.save(usedBook2);


        //when
        chatRoomRepository.findByChatroomWithNickname(nickname);

        //then
        
    }

}