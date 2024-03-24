package team.dankookie.server4983.member.dto;

import team.dankookie.server4983.member.constant.AccountBank;

public record MemberMyPageModifyResponse(
        String imageUrl,
        String nickname,
        AccountBank accountBank,
        String accountNumber,
        String phoneNumber
) {
    public static MemberMyPageModifyResponse of(String imageUrl, String nickname, AccountBank accountBank, String accountNumber, String phoneNumber) {
        return new MemberMyPageModifyResponse(imageUrl, nickname, accountBank, accountNumber, phoneNumber);
    }
}
