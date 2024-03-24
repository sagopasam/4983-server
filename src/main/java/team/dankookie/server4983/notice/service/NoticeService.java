package team.dankookie.server4983.notice.service;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.notice.domain.Notice;
import team.dankookie.server4983.notice.dto.NoticeDetailResponse;
import team.dankookie.server4983.notice.dto.NoticeResponse;
import team.dankookie.server4983.notice.repository.NoticeRepository;
import team.dankookie.server4983.s3.service.S3UploadService;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class NoticeService {

  private final NoticeRepository noticeRepository;
  private final S3UploadService s3Service;

  @Transactional
  public void saveNotice(
      MultipartFile mainFile, MultipartFile noticeFile, MultipartFile noticeWindowFile,
      String title,
      LocalDate startDate,
      LocalDate endDate
  ) {

    String mainFileUrl = s3Service.saveFileWithUUID(mainFile).s3ImageUrl();
    String noticeFileUrl = s3Service.saveFileWithUUID(noticeFile).s3ImageUrl();
    String noticeWindowFileUrl = s3Service.saveFileWithUUID(noticeWindowFile).s3ImageUrl();

    noticeRepository.save(
        Notice.of(
            title,
            startDate,
            endDate,
            mainFileUrl,
            noticeFileUrl,
            noticeWindowFileUrl
        )
    );

  }

  public Page<Notice> getNoticeAdminList(String searchKeyword, int page) {
    PageRequest pageRequest = PageRequest.of(page, 12);

    return noticeRepository.findAllNoticeByTitleContains(searchKeyword, pageRequest);

  }

  @Transactional
  public void updateNotice(Long id, MultipartFile mainFile, MultipartFile noticeFile,
      MultipartFile noticeWindowFile, String title, LocalDate startDate, LocalDate endDate) {

    Notice notice = noticeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

    if (mainFile != null) {
      String mainFileUrl = s3Service.saveFileWithUUID(mainFile).s3ImageUrl();
      notice.setMainBannerImageUrl(mainFileUrl);
    }

    if (noticeFile != null) {
      String noticeFileUrl = s3Service.saveFileWithUUID(noticeFile).s3ImageUrl();
      notice.setNoticeBannerImageUrl(noticeFileUrl);
    }

    if (noticeWindowFile != null) {
      String noticeWindowFileUrl = s3Service.saveFileWithUUID(noticeWindowFile).s3ImageUrl();
      notice.setNoticeWindowImageUrl(noticeWindowFileUrl);
    }

    notice.update(title, startDate, endDate);
  }

  public Notice getNotice(Long id) {
    return noticeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));
  }

  public List<NoticeResponse> getMainNotice() {
    LocalDate now = LocalDate.now();

    return noticeRepository.findAllByNoticeStartDateBeforeAndNoticeEndDateAfter(now).stream()
        .map(NoticeResponse::ofMainBanner).toList();

  }

  public List<NoticeResponse> getNoticeList() {
    LocalDate now = LocalDate.now();

    return noticeRepository.findAllByNoticeStartDateBeforeAndNoticeEndDateAfter(now).stream()
        .map(NoticeResponse::ofNoticeBanner).toList();
  }

  public NoticeDetailResponse getDetailNoticeImageList(Long id) {
    Notice notice = noticeRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

    return NoticeDetailResponse.of(notice.getTitle(), notice.getNoticeWindowImageUrl());
  }

}
