package team.dankookie.server4983.common.dto;

public record BaseMessageResponse(
        String message
) {
    public static BaseMessageResponse of(String message) {
        return new BaseMessageResponse(message);
    }
}
