package team.dankookie.server4983.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountBank {
    WOORI("우리은행"),
    IBK("기업은행"),
    KB("국민은행"),
    KAKAO("카카오뱅크"),
    NH("농협은행"),
    SHINHAN("신한은행"),
    KEBHANA("하나은행"),
    KFCC("새마을금고"),
    EPOST("우체국"),
    SC("SC제일은행"),
    DGB("대구은행"),
    BUSAN("부산은행"),
    KN("경남은행"),
    KJ("광주은행"),
    SHINHYUP("신협"),
    SUHYUP("수협은행"),
    KDB("산업은행"),
    JB("전북은행"),
    JEJU("제주은행"),
    CITI("씨티은행"),
    K("케이뱅크"),
    TOSS("토스뱅크");

    private final String koBankName;
}
