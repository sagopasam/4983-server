package team.dankookie.server4983.member.dto;

public record MemberCertificationRequest(
        String studentId,
        String phoneNumber
) {

    public static MemberCertificationRequest of(String studentId, String phoneNumber) {
        return new MemberCertificationRequest(studentId, phoneNumber);
    }
}
