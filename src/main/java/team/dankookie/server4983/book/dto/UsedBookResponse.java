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
    private final LocalDateTime tradeAvailableDatetime;
    private final Boolean underlinedOrWrite;
    private final Boolean discolorationAndDamage;
    private final Boolean coverDamaged;
    private final Integer price;
    private final BookStatus bookStatus;
    private final Boolean isBookOwner;

    public static UsedBookResponse of(String college, String department, String sellerUserNickname, String sellerProfileImageUrl, LocalDateTime createdAt, List<String> bookImage, String bookName, String publisher, LocalDateTime tradeAvailableDate, Boolean isUnderlinedOrWrite, Boolean isDiscolorationAndDamage, Boolean isCoverDamaged, Integer price, BookStatus bookStatus, Boolean isBookOwner) {
        return new UsedBookResponse(college, department, sellerUserNickname, sellerProfileImageUrl, createdAt, bookImage, bookName, publisher, tradeAvailableDate, isUnderlinedOrWrite, isDiscolorationAndDamage, isCoverDamaged, price, bookStatus, isBookOwner);
    }
}
