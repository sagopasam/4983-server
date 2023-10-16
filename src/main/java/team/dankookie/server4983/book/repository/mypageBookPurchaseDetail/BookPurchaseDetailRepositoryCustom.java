package team.dankookie.server4983.book.repository.mypageBookPurchaseDetail;

import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.dto.UsedBookListResponse;

import java.util.List;

public interface BookPurchaseDetailRepositoryCustom {
    List<UsedBookListResponse> getMyPageBookPurchaseDetailList(BookStatus bookStatus, Long memberId);
}
