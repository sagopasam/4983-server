package team.dankookie.server4983.book.repository.mypageBookSalesDetail;

import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;

public interface BookSalesDetailRepositoryCustom {
    List<UsedBook> getMyPageBookSalesDetailList(boolean canBuy, Long memberId);
}
