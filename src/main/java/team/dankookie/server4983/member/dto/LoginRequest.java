package team.dankookie.server4983.member.dto;

public record LoginRequest(
        String studentId,
        String password
) {
    public static LoginRequest of(String studentId, String password) {
        return new LoginRequest(studentId, password);
    }
}
