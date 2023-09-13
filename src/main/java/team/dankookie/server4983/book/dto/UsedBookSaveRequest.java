package team.dankookie.server4983.book.dto;

import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;
import team.dankookie.server4983.member.domain.Member;

import java.time.LocalDate;

public record UsedBookSaveRequest(
        College college,
        Department department,
        int price,
        LocalDate tradeAvailableDate,
        String name,
        String publisher,
        boolean isUnderlinedOrWrite,
        boolean isDiscolorationAndDamage,
        boolean isCoverDamaged
) {
    public static UsedBookSaveRequest of(College college, Department department, int price, LocalDate tradeAvailableDate, String name, String publisher, boolean isUnderlinedOrWrite, boolean isDiscolorationAndDamage, boolean isCoverDamaged) {
        return new UsedBookSaveRequest(college, department, price, tradeAvailableDate, name, publisher, isUnderlinedOrWrite, isDiscolorationAndDamage, isCoverDamaged);
    }

    public UsedBook toEntity(Member member) {
        return UsedBook.builder()
                .college(college)
                .department(department)
                .price(price)
                .tradeAvailableDate(tradeAvailableDate)
                .name(name)
                .publisher(publisher)
                .isUnderlinedOrWrite(isUnderlinedOrWrite)
                .isDiscolorationAndDamage(isDiscolorationAndDamage)
                .isCoverDamaged(isCoverDamaged)
                .sellerMember(member)
                .bookStatus(BookStatus.SALE)
                .build();
    }
}
