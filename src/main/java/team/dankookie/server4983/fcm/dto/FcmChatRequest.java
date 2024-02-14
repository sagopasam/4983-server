package team.dankookie.server4983.fcm.dto;

public record FcmChatRequest(
    Long targetUserId,
    String message,
    String screenName,
    Long chatRoomId

) {

  public static FcmChatRequest of(Long targetUserId, String message, String screenName,
      Long chatRoomId) {
    return new FcmChatRequest(targetUserId, message, screenName, chatRoomId);
  }

}
