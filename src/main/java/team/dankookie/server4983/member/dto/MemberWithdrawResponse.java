package team.dankookie.server4983.member.dto;

public record MemberWithdrawResponse(
    Boolean isWithdraw
) {

  public static MemberWithdrawResponse of(Boolean isWithdraw) {
    return new MemberWithdrawResponse(isWithdraw);
  }
}
