package team.dankookie.server4983.book.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
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

    //FIXME. 기획에 따라 LocalDatetime, LocalDate 로 변경 간으
    @NotNull
    private LocalDate tradeAvailableDate;

    private String publisher;

    @Enumerated(EnumType.STRING)
    private College college;

    @Enumerated(EnumType.STRING)
    private Department department;

    @Column(columnDefinition = "boolean default false")
    private Boolean isUnderlinedOrWrite;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDiscolorationAndDamage;

    @Column(columnDefinition = "boolean default false")
    private Boolean isCoverDamaged;

    @Column(columnDefinition = "boolean default true")
    private Boolean onSailed;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member buyerMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member sellerMember;


}
