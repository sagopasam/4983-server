package team.dankookie.server4983.fcm.dto;

public record FcmResponse(
        String message
) {
    public static FcmResponse of(String message) {
        return new FcmResponse(message);
    }
}
