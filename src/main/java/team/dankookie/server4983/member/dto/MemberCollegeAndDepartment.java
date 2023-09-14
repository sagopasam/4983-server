package team.dankookie.server4983.member.dto;

import team.dankookie.server4983.book.constant.Department;

public record MemberCollegeAndDepartment(
        String college,
        String department
) {

    public static MemberCollegeAndDepartment of(Department department) {
        return new MemberCollegeAndDepartment(department.getCollege().name(), department.name());
    }
}
