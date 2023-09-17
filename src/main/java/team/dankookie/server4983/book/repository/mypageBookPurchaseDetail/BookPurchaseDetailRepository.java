package team.dankookie.server4983.book.repository.mypageBookPurchaseDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.book.domain.UsedBook;

public interface BookPurchaseDetailRepository extends JpaRepository<UsedBook, Long>, BookPurchaseDetailRepositoryCustom {
}
