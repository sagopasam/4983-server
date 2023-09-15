package team.dankookie.server4983.book.repository.usedBook;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.member.domain.Member;

public interface UsedBookRepository extends JpaRepository<UsedBook, Long>, UsedBookRepositoryCustom {

    boolean existsUsedBookByIdAndSellerMember(Long id, Member member);
}
