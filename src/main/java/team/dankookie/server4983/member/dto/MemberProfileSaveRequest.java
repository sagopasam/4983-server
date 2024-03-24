package team.dankookie.server4983.member.dto;

import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;

public record MemberProfileSaveRequest(

        String nickname,
        AccountBank accountBank,
        String accountNumber,
        String phoneNumber
) {
    public static MemberProfileSaveRequest of(String nickname, AccountBank accountBank, String accountNumber, String phoneNumber){
        return new MemberProfileSaveRequest(nickname, accountBank, accountNumber, phoneNumber);
    }
}
