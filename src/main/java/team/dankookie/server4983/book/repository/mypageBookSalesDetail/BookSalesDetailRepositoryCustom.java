package team.dankookie.server4983.book.repository.mypageBookSalesDetail;

import team.dankookie.server4983.book.dto.UsedBookListResponse;

import java.util.List;

public interface BookSalesDetailRepositoryCustom {
    List<UsedBookListResponse> getMyPageBookSalesDetailList(boolean canBuy, Long memberId);
}
