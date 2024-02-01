package team.dankookie.server4983.notice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notice {

  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private String title;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  @Setter
  @NotNull
  private String mainBannerImageUrl;

  @Setter
  @NotNull
  private String noticeBannerImageUrl;

  @Setter
  @NotNull
  private String noticeWindowImageUrl;

  private Notice(Long id, String title, LocalDate startDate, LocalDate endDate,
      String mainBannerImageUrl, String noticeBannerImageUrl, String noticeWindowImageUrl) {
    this.id = id;
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
    this.mainBannerImageUrl = mainBannerImageUrl;
    this.noticeBannerImageUrl = noticeBannerImageUrl;
    this.noticeWindowImageUrl = noticeWindowImageUrl;
  }

  public static Notice of(
      String title,
      LocalDate startDate,
      LocalDate endDate,
      String mainBannerImageUrl,
      String noticeBannerImageUrl,
      String noticeWindowImageUrl
  ) {
    return new Notice(null, title, startDate, endDate, mainBannerImageUrl, noticeBannerImageUrl,
        noticeWindowImageUrl);
  }

  public void update(String title, LocalDate startDate, LocalDate endDate) {
    this.title = title;
    this.startDate = startDate;
    this.endDate = endDate;
  }
}
