package team.dankookie.server4983.member.dto;

import team.dankookie.server4983.member.domain.Member;

public record MemberImageRequest(
        String image
) {
    public static MemberImageRequest of(String image){
        return new MemberImageRequest(image);
    }

    public Member toEntity(String nickname){
        return Member.builder()
                .imageUrl("https://4983-s3.s3.ap-northeast-2.amazonaws.com/baseImage.png")
                .build();
    }
}
