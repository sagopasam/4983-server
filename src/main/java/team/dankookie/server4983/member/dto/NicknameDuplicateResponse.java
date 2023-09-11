package team.dankookie.server4983.member.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NicknameDuplicateResponse {
    private final boolean nicknameDuplicate;

    public static NicknameDuplicateResponse of(boolean isDuplicate) {
        return new NicknameDuplicateResponse(isDuplicate);
    }
}
