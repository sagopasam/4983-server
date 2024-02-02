package team.dankookie.server4983.notice.repository;

import java.time.LocalDate;
import java.util.List;
import team.dankookie.server4983.notice.domain.Notice;

public interface NoticeRepositoryCustom {

  List<Notice> findAllByNoticeStartDateBeforeAndNoticeEndDateAfter(LocalDate date);
}
