package team.dankookie.server4983.member.repository;

import static team.dankookie.server4983.member.domain.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.AdminMemberListResponse;
import team.dankookie.server4983.member.dto.QAdminMemberListResponse;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminMemberListResponse> getMember(Pageable pageable, String searchKeyword) {

        Department department = Department.getByKoName(searchKeyword);
        List<AdminMemberListResponse> content = jpaQueryFactory
                .select(
                        new QAdminMemberListResponse(
                                member.id,
                                member.studentId,
                                member.department,
                                member.nickname,
                                member.phoneNumber,
                                member.accountBank,
                                member.accountNumber,
                                member.accountHolder,
                                member.isWithdraw
                        )
                ).from(member)
                .where(studentIdOrNicknameAndPhoneNumberLike(searchKeyword).or(isSearchKeywordDepartment(department))
                )
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(12)
                .fetch();

        Long count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(studentIdOrNicknameAndPhoneNumberLike(searchKeyword)).fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    private static BooleanExpression isSearchKeywordDepartment(Department department) {

        if (department == Department.DEFAULT) {
            return null;
        }
        return member.department.eq(department);
    }

    @Override
    public Page<AdminMemberListResponse> getBlockedMember(Pageable pageable, String searchKeyword) {
        List<AdminMemberListResponse> content = jpaQueryFactory
                .select(
                        new QAdminMemberListResponse(
                                member.id,
                                member.studentId,
                                member.department,
                                member.nickname,
                                member.phoneNumber,
                                member.accountBank,
                                member.accountNumber,
                                member.accountHolder,
                                member.isWithdraw
                        )
                ).from(member)
                .where(
                        member.isWithdraw.eq(true),
                        studentIdOrNicknameAndPhoneNumberLike(searchKeyword)
                )
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(12)
                .fetch();

        Long count = jpaQueryFactory
                .select(member.count())
                .from(member)
                .where(
                        member.isWithdraw.eq(true),
                        studentIdOrNicknameAndPhoneNumberLike(searchKeyword)
                ).fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<Member> findByPushCondition(final List<String> memberIds, final List<Department> departments,
                                            final List<Integer> yearOfAdmissions) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(
                        pushCondition(memberIds, departments, yearOfAdmissions)
                ).fetch();
    }

    private BooleanBuilder pushCondition(final List<String> memberIds, final List<Department> departments,
                                         final List<Integer> yearOfAdmissions) {
        final BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!CollectionUtils.isEmpty(memberIds)) {
            booleanBuilder.and(member.studentId.in(memberIds));
        }

        if (!CollectionUtils.isEmpty(departments)) {
            booleanBuilder.and(member.department.in(departments));
        }

        if (!CollectionUtils.isEmpty(yearOfAdmissions)) {
            booleanBuilder.and(member.yearOfAdmission.in(yearOfAdmissions));
        }

        return booleanBuilder;
    }

    private BooleanExpression studentIdOrNicknameAndPhoneNumberLike(String searchKeyword) {
        return member.studentId.contains(searchKeyword)
                .or(member.nickname.contains(searchKeyword))
                .or(member.phoneNumber.contains(searchKeyword));
    }
}
