package team.dankookie.server4983.member.dto;


public record MemberPasswordMatchResponse(
        boolean isPasswordMatch
){

    public static MemberPasswordMatchResponse of(boolean isPasswordMatch) {
        return new MemberPasswordMatchResponse(isPasswordMatch);
    }
}
