package team.dankookie.server4983.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.UsedBookSaveRequest;
import team.dankookie.server4983.common.domain.BaseEntity;
import team.dankookie.server4983.member.domain.Member;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UsedBook extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private LocalDate tradeAvailableDate;

    private String publisher;

    @Enumerated(EnumType.STRING)
    private College college;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;

    @Column(columnDefinition = "boolean default false")
    private Boolean isUnderlinedOrWrite;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDiscolorationAndDamage;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCoverDamaged;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member buyerMember;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member sellerMember;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDeleted;

    public void setIsDeletedTrue() {
        isDeleted = true;
    }
    @Builder
    public UsedBook(Long id, String name, Integer price, LocalDate tradeAvailableDate, String publisher, College college, Department department, BookStatus bookStatus, Boolean isUnderlinedOrWrite, Boolean isDiscolorationAndDamage, Boolean isCoverDamaged, Member buyerMember, Member sellerMember, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tradeAvailableDate = tradeAvailableDate;
        this.publisher = publisher;
        this.college = college;
        this.department = department;
        this.bookStatus = bookStatus;
        this.isUnderlinedOrWrite = isUnderlinedOrWrite;
        this.isDiscolorationAndDamage = isDiscolorationAndDamage;
        this.isCoverDamaged = isCoverDamaged;
        this.buyerMember = buyerMember;
        this.sellerMember = sellerMember;
        this.isDeleted = isDeleted;
    }

    public void updateUsedBook(UsedBookSaveRequest usedBook) {
        this.name = usedBook.name();
        this.price = usedBook.price();
        this.tradeAvailableDate = usedBook.tradeAvailableDate();
        this.publisher = usedBook.publisher();
        this.college = usedBook.college();
        this.department = usedBook.department();
        this.isUnderlinedOrWrite = usedBook.isUnderlinedOrWrite();
        this.isDiscolorationAndDamage = usedBook.isDiscolorationAndDamage();
        this.isCoverDamaged = usedBook.isCoverDamaged();
    }
}
