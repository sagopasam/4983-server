package team.dankookie.server4983.book.dto;

import lombok.Builder;
import team.dankookie.server4983.book.constant.BookStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MyPageBookSalesDetailListResponse(
        String imageUrl,
        BookStatus bookStatus,
        String name,
        LocalDate tradeAvailableDate,
        LocalDateTime createdAt,
        Integer price
) {
    @Builder
    public MyPageBookSalesDetailListResponse {

    }
}
