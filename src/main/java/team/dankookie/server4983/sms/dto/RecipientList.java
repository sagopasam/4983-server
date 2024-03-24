package team.dankookie.server4983.sms.dto;

public record RecipientList(
        String recipientNo
) {
    public static RecipientList of(String recipientNo) {
        return new RecipientList(recipientNo);
    }
}
