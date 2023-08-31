package team.dankookie.server4983.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;

@RequiredArgsConstructor
@Getter
public class MemberPasswordChangeRequest {
    private final String studentId;
    private final String phoneNumber;
    private final String password;

    public static MemberPasswordChangeRequest of(String studentId, String phoneNumber, String password) {
        return new MemberPasswordChangeRequest(studentId, phoneNumber, password);
    }
}
