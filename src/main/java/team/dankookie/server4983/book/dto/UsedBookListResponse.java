package team.dankookie.server4983.book.dto;

import lombok.Builder;
import team.dankookie.server4983.book.constant.BookStatus;

import java.time.LocalDateTime;

public record UsedBookListResponse (
        String imageUrl,
        BookStatus bookStatus,
        String name,
        LocalDateTime tradeAvailableDatetime,
        LocalDateTime createdAt,
        Integer price
){

    @Builder
    public UsedBookListResponse {
    }
}

