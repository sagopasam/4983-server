package team.dankookie.server4983.notice.dto;

import team.dankookie.server4983.notice.domain.Notice;

public record NoticeResponse(
    Long id,
    String imageUrl
) {

  public static NoticeResponse of(Long id, String imageUrl) {
    return new NoticeResponse(id, imageUrl);
  }

  public static NoticeResponse ofMainBanner(Notice notice) {
    return new NoticeResponse(notice.getId(), notice.getMainBannerImageUrl());
  }

  public static NoticeResponse ofNoticeBanner(Notice notice) {
    return new NoticeResponse(notice.getId(), notice.getNoticeBannerImageUrl());
  }
}
