package team.dankookie.server4983.book.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static team.dankookie.server4983.book.constant.College.*;

@RequiredArgsConstructor
@Getter
public enum Department {
    GE("교양", College.GE),
    BUSINESS("경영학과", BUSINESS_AND_ECONOMICS),
    ECONOMICS("경제학과", BUSINESS_AND_ECONOMICS),
    INTERNATIONAL_BUSINESS("국제경영학전공", BUSINESS_AND_ECONOMICS),
    TRADE("무역학과", BUSINESS_AND_ECONOMICS),
    INDUSTRIAL_BUSINESS("산업경영학과", BUSINESS_AND_ECONOMICS),
    ACCOUNTING("회계학과", BUSINESS_AND_ECONOMICS),

    POLITICAL("정치외교학과", SOCIAL_SCIENCES),
    PUBLIC_ADMINISTRATION("행정학과", SOCIAL_SCIENCES),
    URBAN_PLANNING("도시계획부동산학부", SOCIAL_SCIENCES),
    MEDIA_COMMUNICATION("미디어커뮤니케이션학부", SOCIAL_SCIENCES),
    CONSULTING("상담학과", SOCIAL_SCIENCES),

    DEPARTMENT_OF_LAW("법학과", LAW),

    KOREAN("국어국문과", LITERAL_ARTS),
    HISTORY("사학과", LITERAL_ARTS),
    PHILOSOPHY("철학과", LITERAL_ARTS),
    AMERICAN_HUMANITY("영미인문학과", LITERAL_ARTS),

    ELECTRICAL("전자전기공학전공", ENGINEERING),
    POLYMER("고분자공학전공", ENGINEERING),
    FUSION_SEMICONDUCTOR("융합반도체공학전공", ENGINEERING),
    FIBER("파이버융합소재공학전공", ENGINEERING),
    CHEMICAL("화학공학과", ENGINEERING),
    ARCHITECTURE_ENGINEER("건축공학전공", ENGINEERING),
    ARCHITECTURE("건축학전공", ENGINEERING),

    SOFTWARE("소프트웨어학과", SW_CONVERGENCE),
    COMPUTER("컴퓨터공학과", SW_CONVERGENCE),
    MOBILE_SYSTEM("모바일시스템공학과", SW_CONVERGENCE),
    STATISTICS("정보통계학과", SW_CONVERGENCE),
    SECURITY("산업보안학과", SW_CONVERGENCE),

    MATH("수학교육과", EDUCATION),
    SCIENCE("과학교육과", EDUCATION),
    ATHLETIC("체육교육과", EDUCATION),
    CHINESE("한문교육과", EDUCATION),
    SPECIAL("특수교육과", EDUCATION),

    MOVIE("영화전공", MUSIC_AND_ARTS),
    THEATER("연극전공", MUSIC_AND_ARTS),
    MUSICAL("뮤지컬전공", MUSIC_AND_ARTS),
    CERAMICS("도예과", MUSIC_AND_ARTS),
    COMMUNICATION_DESIGN("커뮤니케이션디자인전공", MUSIC_AND_ARTS),
    INDUSTRIAL_DESIGN("패션산업디자인전공", MUSIC_AND_ARTS),
    DANCING("무용과", MUSIC_AND_ARTS),
    PIANO("피아노전공", MUSIC_AND_ARTS),
    VOCAL("성악전공", MUSIC_AND_ARTS),
    ORCHESTRA("관현악전공", MUSIC_AND_ARTS),
    COMPOSITION("작곡전공", MUSIC_AND_ARTS),
    GUGAK("국악전공", MUSIC_AND_ARTS);

    private final String koName;
    private final College college;
}
