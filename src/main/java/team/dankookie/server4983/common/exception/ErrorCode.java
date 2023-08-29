package team.dankookie.server4983.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_TOKEN(UNAUTHORIZED, "잘못된 토큰입니다."),
    ;

    private final HttpStatus status;
    private final String message;

}
