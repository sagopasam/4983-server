package team.dankookie.server4983.jwt.dto;

public record AccessToken (
        String value
) {
    public static AccessToken of(String value) {
        return new AccessToken(value);
    }
}
