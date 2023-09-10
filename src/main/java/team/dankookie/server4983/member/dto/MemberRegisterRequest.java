package team.dankookie.server4983.member.dto;

import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;

public record MemberRegisterRequest(
        String studentId,
        Department department,
        int yearOfAdmission,
        String nickname,
        String password,
        String phoneNumber,
        boolean marketingAgree,
        String accountHolder,
        AccountBank accountBank,
        String accountNumber

) {

    public static MemberRegisterRequest of(String studentId,
                                           Department department,
                                           int yearOfAdmission,
                                           String nickname,
                                           String password,
                                           String phoneNumber,
                                           boolean marketingAgree,
                                           String accountHolder,
                                           AccountBank accountBank,
                                           String accountNumber
    ) {
        return new MemberRegisterRequest(
                studentId,
                department,
                yearOfAdmission,
                nickname,
                password,
                phoneNumber,
                marketingAgree,
                accountHolder,
                accountBank,
                accountNumber
        );
    }

    public Member toEntity() {
        return Member.builder()
                .studentId(studentId)
                .department(department)
                .yearOfAdmission(yearOfAdmission)
                .nickname(nickname)
                .password(password)
                .phoneNumber(phoneNumber)
                .marketingAgree(marketingAgree)
                .accountHolder(accountHolder)
                .accountBank(accountBank)
                .accountNumber(accountNumber)
                .build();
    }
}
