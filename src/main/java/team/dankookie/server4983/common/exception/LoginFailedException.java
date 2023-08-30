package team.dankookie.server4983.common.exception;

public class LoginFailedException extends RuntimeException {

    public LoginFailedException() {
        super("로그인에 실패하였습니다.");
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
