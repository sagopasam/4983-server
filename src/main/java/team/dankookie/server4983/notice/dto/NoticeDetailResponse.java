package team.dankookie.server4983.notice.dto;

public record NoticeDetailResponse(
    String title,
    String imageUrl
) {

  public static NoticeDetailResponse of(String title, String imageUrl) {
    return new NoticeDetailResponse(title, imageUrl);
  }

}
