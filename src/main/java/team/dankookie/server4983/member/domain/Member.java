package team.dankookie.server4983.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import team.dankookie.server4983.common.domain.BaseEntity;
import team.dankookie.server4983.member.constant.AccountBank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String studentId;

    @NotNull
    private String department;

    @NotNull
    private Integer yearOfAdmission;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String accountHolder;


    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountBank accountBank;

    @NotNull
    private String accountNumber;

    private String imageUrl;

    @Column(columnDefinition = "boolean default false")
    private Boolean marketingAgree;

    @Builder
    public Member(String studentId, String department, Integer yearOfAdmission, String nickname, String password, String phoneNumber, String accountHolder, AccountBank accountBank, String accountNumber) {
        this.studentId = studentId;
        this.department = department;
        this.yearOfAdmission = yearOfAdmission;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.accountHolder = accountHolder;
        this.accountBank = accountBank;
        this.accountNumber = accountNumber;
    }



}
