package team.dankookie.server4983.notice.controller;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.notice.domain.Notice;
import team.dankookie.server4983.notice.service.NoticeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

  private final NoticeService noticeService;

  @PostMapping
  public ResponseEntity<Void> saveNotice(
      @RequestPart("mainFile") MultipartFile mainFile,
      @RequestPart("noticeFile") MultipartFile noticeFile,
      @RequestPart("noticeWindowFile") MultipartFile noticeWindowFile,
      @RequestPart("title") String title,
      @RequestPart("startDate") LocalDate startDate,
      @RequestPart("endDate") LocalDate endDate
  ) {

    noticeService.saveNotice(mainFile, noticeFile, noticeWindowFile, title, startDate, endDate);

    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<Page<Notice>> getNoticeList(
      @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
      @RequestParam(value = "page", defaultValue = "0") int page
  ) {
    return ResponseEntity.ok(noticeService.getNoticeList(searchKeyword, page));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Notice> getNotice(@PathVariable("id") Long id) {
    return ResponseEntity.ok(noticeService.getNotice(id));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateNotice(
      @PathVariable("id") Long id,
      @RequestPart("mainFile") MultipartFile mainFile,
      @RequestPart("noticeFile") MultipartFile noticeFile,
      @RequestPart("noticeWindowFile") MultipartFile noticeWindowFile,
      @RequestPart("title") String title,
      @RequestPart("startDate") LocalDate startDate,
      @RequestPart("endDate") LocalDate endDate
  ) {
    noticeService.updateNotice(id, mainFile, noticeFile, noticeWindowFile, title, startDate,
        endDate);
    return ResponseEntity.noContent().build();
  }
}
