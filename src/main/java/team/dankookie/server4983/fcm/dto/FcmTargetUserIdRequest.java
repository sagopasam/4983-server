package team.dankookie.server4983.fcm.dto;

public record FcmTargetUserIdRequest(
        Long targetUserId,
        String message
) {
    public static FcmTargetUserIdRequest of(Long targetUserId, String message) {
        return new FcmTargetUserIdRequest(targetUserId, message);
    }
}
