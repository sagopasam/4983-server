package team.dankookie.server4983.jwt.dto;

public record AccessToken (
        String value,
        String studentId
) {
    public static AccessToken of(String value, String studentId) {
        return new AccessToken(value, studentId);
    }
}
