package team.dankookie.server4983.sms.dto;

public record SmsMessageDto(
        String to,
        String content
) {
    public static SmsMessageDto of(String to, String content) {
        return new SmsMessageDto(to, content);
    }
}
