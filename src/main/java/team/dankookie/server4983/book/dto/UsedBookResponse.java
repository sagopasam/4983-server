package team.dankookie.server4983.book.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class UsedBookResponse {
    private final String college;
    private final String department;
    private final String sellerUserNickname;
    private final String sellerProfileImageUrl;
    private final LocalDateTime createdAt;
    private final List<String> bookImage;
    private final String bookName;
    private final String publisher;
    private final LocalDate tradeAvailableDate;
    private final boolean underlinedOrWrite;
    private final boolean discolorationAndDamage;
    private final boolean coverDamaged;
    private final Integer price;
    private final BookStatus bookStatus;

    public static UsedBookResponse of(String college, String department, String sellerUserNickname, String sellerProfileImageUrl, LocalDateTime createdAt, List<String> bookImage, String bookName, String publisher, LocalDate tradeAvailableDate, boolean isUnderlinedOrWrite, boolean isDiscolorationAndDamage, boolean isCoverDamaged, Integer price, BookStatus bookStatus) {
        return new UsedBookResponse(college, department, sellerUserNickname, sellerProfileImageUrl, createdAt, bookImage, bookName, publisher, tradeAvailableDate, isUnderlinedOrWrite, isDiscolorationAndDamage, isCoverDamaged, price, bookStatus);
    }
}
