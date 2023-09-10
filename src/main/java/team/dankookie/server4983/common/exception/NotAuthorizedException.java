package team.dankookie.server4983.common.exception;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException() {
        super("존재하지 않는 토큰입니다.");
    }

    public NotAuthorizedException(String message) {
        super(message);
    }
}
