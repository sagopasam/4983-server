package team.dankookie.server4983.member.repository;

import static team.dankookie.server4983.member.domain.QMember.member;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import team.dankookie.server4983.member.dto.AdminMemberListResponse;
import team.dankookie.server4983.member.dto.QAdminMemberListResponse;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<AdminMemberListResponse> getMember(Pageable pageable, String searchKeyword) {
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
                member.isBlocked
            )
        ).from(member)
        .where(studentIdOrNicknameAndPhoneNumberLike(searchKeyword))
        .orderBy(member.id.desc())
        .offset(pageable.getOffset())
        .limit(12)
        .fetch();

    Long count = jpaQueryFactory
        .select(member.count())
        .from(member)
        .where(studentIdOrNicknameAndPhoneNumberLike(searchKeyword)).fetchOne();

    return new PageImpl<>(content, pageable, count);  }

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
                member.isBlocked
            )
        ).from(member)
        .where(
            member.isBlocked.eq(true),
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
            member.isBlocked.eq(true),
            studentIdOrNicknameAndPhoneNumberLike(searchKeyword)
        ).fetchOne();

    return new PageImpl<>(content, pageable, count);  }

  private BooleanExpression studentIdOrNicknameAndPhoneNumberLike(String searchKeyword) {
    return member.studentId.contains(searchKeyword)
        .or(member.nickname.contains(searchKeyword))
        .or(member.phoneNumber.contains(searchKeyword));
  }
}
