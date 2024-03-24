package team.dankookie.server4983.member.dto;

public record MemberTemporaryPasswordChangeRequest(
        String studentId,

        String phoneNumber,

        String password
){

    public static MemberTemporaryPasswordChangeRequest of(String studentId, String phoneNumber, String password) {
        return new MemberTemporaryPasswordChangeRequest(studentId, phoneNumber, password);
    }
}
