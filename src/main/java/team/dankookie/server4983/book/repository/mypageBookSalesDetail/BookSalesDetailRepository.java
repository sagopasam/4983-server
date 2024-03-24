package team.dankookie.server4983.book.repository.mypageBookSalesDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.UsedBook;

public interface BookSalesDetailRepository extends JpaRepository<UsedBook, Long>, BookSalesDetailRepositoryCustom {
}
