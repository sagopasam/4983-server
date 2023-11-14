package team.dankookie.server4983.member.dto;

public record MemberPasswordRequest(
    String password
) {
  public static MemberPasswordRequest of(String password) {
    return new MemberPasswordRequest(password);
  }
}
