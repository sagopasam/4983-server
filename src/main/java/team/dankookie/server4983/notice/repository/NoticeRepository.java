package team.dankookie.server4983.notice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import team.dankookie.server4983.notice.domain.Notice;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

  Page<Notice> findAllNoticeByTitleContains(String title, PageRequest pageRequest);

}
