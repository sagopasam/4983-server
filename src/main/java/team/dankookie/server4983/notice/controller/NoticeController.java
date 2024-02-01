package team.dankookie.server4983.notice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.dankookie.server4983.notice.dto.NoticeResponse;
import team.dankookie.server4983.notice.service.NoticeService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notice")
public class NoticeController {

  private final NoticeService noticeService;

  @GetMapping("/main")
  public ResponseEntity<List<NoticeResponse>> getMainNotice() {
    return ResponseEntity.ok(noticeService.getMainNotice());
  }

  @GetMapping("/list")
  public ResponseEntity<List<NoticeResponse>> getNoticeList() {
    return ResponseEntity.ok(noticeService.getNoticeList());
  }

  @GetMapping("/detail/{id}")
  public ResponseEntity<String> getNoticeDetail(
      @PathVariable("id") Long id
  ) {
    return ResponseEntity.ok(noticeService.getDetailNoticeImageList(id));
  }
}
