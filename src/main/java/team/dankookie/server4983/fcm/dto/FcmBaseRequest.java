package team.dankookie.server4983.fcm.dto;

import java.util.List;
import team.dankookie.server4983.member.domain.Member;

public record FcmBaseRequest(
         List<Member> members,
         String title,
         String body
) {

    public static FcmBaseRequest of(List<Member> members, String title, String body) {
        return new FcmBaseRequest(members, title, body);
    }
}
