package team.dankookie.server4983.member.fixture;

import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;

public class MemberFixture {

    public static final Member createMember() {
        return Member.builder()
                .accountBank(AccountBank.K)
                .accountHolder("accountHolder")
                .accountNumber("accountNumber")
                .nickname("nickname")
                .studentId("studentId")
                .phoneNumber("phoneNumber")
                .department(Department.DEPARTMENT_OF_LAW)
                .yearOfAdmission(2023)
                .password("password")
                .build();
    }

    public static Member createMemberByNickname(String nickname) {
        return Member.builder()
                .accountBank(AccountBank.K)
                .accountHolder("accountHolder")
                .accountNumber("accountNumber")
                .nickname(nickname)
                .studentId("studentId")
                .phoneNumber("phoneNumber")
                .department(Department.DEPARTMENT_OF_LAW)
                .yearOfAdmission(2023)
                .password("password")
                .build();
    }

    public static Member createMemberByStudentId(String studentId) {
        return Member.builder()
                .accountBank(AccountBank.K)
                .accountHolder("accountHolder")
                .accountNumber("accountNumber")
                .nickname("nickname")
                .studentId(studentId)
                .phoneNumber("phoneNumber")
                .department(Department.DEPARTMENT_OF_LAW)
                .yearOfAdmission(2023)
                .password("password")
                .build();
    }
}
