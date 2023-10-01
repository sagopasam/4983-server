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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDateTime tradeAvailableDatetime;

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

    @OneToMany(mappedBy = "usedBook", cascade = CascadeType.ALL)
    private List<BookImage> bookImageList = new ArrayList<>();


    // FIXME 테스트 용 USEDBOOK 생성자 ( 테스트 데이터가 필요 없어진게 아니면 절대 수정하지 마세요!! )
    public UsedBook(long id, String bookName, int price, LocalDateTime tradeAvailableDateTime, String publisher, College college, Department department, BookStatus bookStatus, boolean isUnderlinedOrWrite, boolean isDiscolorationAndDamage, boolean isCoverDamaged, Member buyer, Member seller) {
        this.id = id;
        this.name = bookName;
        this.price = price;
        this.tradeAvailableDatetime = tradeAvailableDateTime;
        this.publisher = publisher;
        this.college = college;
        this.department = department;
        this.bookStatus = bookStatus;
        this.isUnderlinedOrWrite = isUnderlinedOrWrite;
        this.isDiscolorationAndDamage = isDiscolorationAndDamage;
        this.isCoverDamaged = isCoverDamaged;
        this.buyerMember = buyer;
        this.sellerMember = seller;
        this.isDeleted = false;
    }

    public void setIsDeletedTrue() {
        isDeleted = true;
    }

    @Builder
    public UsedBook(Long id, String name, Integer price, LocalDateTime tradeAvailableDatetime, String publisher, College college, Department department, BookStatus bookStatus, Boolean isUnderlinedOrWrite, Boolean isDiscolorationAndDamage, Boolean isCoverDamaged, Member buyerMember, Member sellerMember, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.tradeAvailableDatetime = tradeAvailableDatetime;
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
        this.tradeAvailableDatetime = usedBook.tradeAvailableDatetime();
        this.publisher = usedBook.publisher();
        this.college = usedBook.college();
        this.department = usedBook.department();
        this.isUnderlinedOrWrite = usedBook.isUnderlinedOrWrite();
        this.isDiscolorationAndDamage = usedBook.isDiscolorationAndDamage();
        this.isCoverDamaged = usedBook.isCoverDamaged();
    }
}
