package team.dankookie.server4983.book.repository.mypageBookPurchaseDetail;

import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;

public interface BookPurchaseDetailRepositoryCustom {
    List<UsedBook> getMyPageBookPurchaseDetailList(BookStatus bookStatus, Long memberId);
}
