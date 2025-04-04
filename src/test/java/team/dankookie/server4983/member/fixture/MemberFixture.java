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
                .nickname("studentId")
                .studentId("studentId")
                .phoneNumber("phoneNumber")
                .college(Department.DEPARTMENT_OF_LAW.getCollege())
                .department(Department.DEPARTMENT_OF_LAW)
                .yearOfAdmission(2023)
                .password("password")
                .build();
    }

    public static final Member createMemberByFirebaseToken(String token) {
        return Member.builder()
                .accountBank(AccountBank.K)
                .accountHolder("accountHolder")
                .accountNumber("accountNumber")
                .firebaseToken(token)
                .nickname("studentId")
                .studentId("studentId")
                .phoneNumber("phoneNumber")
                .college(Department.DEPARTMENT_OF_LAW.getCollege())
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
                .college(Department.DEPARTMENT_OF_LAW.getCollege())
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
                .college(Department.DEPARTMENT_OF_LAW.getCollege())
                .department(Department.DEPARTMENT_OF_LAW)
                .yearOfAdmission(2023)
                .password("password")
                .build();
    }

    public static Member createMemberByStudentIdAndNickname(String studentId, String nickname) {
        return Member.builder()
            .accountBank(AccountBank.K)
            .accountHolder("accountHolder")
            .accountNumber("accountNumber")
            .nickname(nickname)
            .studentId(studentId)
            .phoneNumber("phoneNumber")
            .college(Department.DEPARTMENT_OF_LAW.getCollege())
            .department(Department.DEPARTMENT_OF_LAW)
            .yearOfAdmission(2023)
            .password("password")
            .build();
    }
}
