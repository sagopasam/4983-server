package team.dankookie.server4983.fcm.dto;

public record FcmTokenRequest(
    String token
) {

  public static FcmTokenRequest of(String token) {
    return new FcmTokenRequest(token);
  }
}
