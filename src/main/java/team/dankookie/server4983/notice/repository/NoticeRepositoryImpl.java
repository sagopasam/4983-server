package team.dankookie.server4983.notice.repository;

import static team.dankookie.server4983.notice.domain.QNotice.notice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.notice.domain.Notice;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<Notice> findAllByNoticeStartDateBeforeAndNoticeEndDateAfter(LocalDate date) {
    return jpaQueryFactory
        .selectFrom(notice)
        .where(notice.startDate.goe(date).and(notice.endDate.loe(date)))
        .fetch();
  }
}
