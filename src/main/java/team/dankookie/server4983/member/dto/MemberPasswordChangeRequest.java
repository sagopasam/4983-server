package team.dankookie.server4983.member.dto;

public record MemberPasswordChangeRequest(
        String password
) {
    public static MemberPasswordChangeRequest of(String password) {
        return new MemberPasswordChangeRequest(password);
    }
}
