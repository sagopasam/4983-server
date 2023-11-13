package team.dankookie.server4983.member.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.jwt.constants.TokenDuration;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.domain.Member;
import team.dankookie.server4983.member.dto.MemberMyPageModifyResponse;
import team.dankookie.server4983.member.service.MemberService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberMyPageControllerTest extends BaseControllerTest {

    @MockBean
    MemberService memberService;
    @Test
    void 마이페이지_수정시_멤버의_데이터를_가져온다() throws Exception {
        //given
        String accessToken = jwtTokenUtils.generateJwtToken("nickname",  TokenDuration.ACCESS_TOKEN_DURATION.getDuration());
        Member findMember = Member.builder().nickname("testNickname").build();
        MemberMyPageModifyResponse response = MemberMyPageModifyResponse.of("test.png", findMember.getNickname(), AccountBank.KB, "938002-00-613983", "010-8766-5450");
        when(memberService.getMyPageMemberModifyInfo(anyString())).thenReturn(response);
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/my-pages/member/modify")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
        ).andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("my-pages/member/modifyInfo/success",
                        requestHeaders(
                                headerWithName(org.springframework.http.HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        responseFields(
                                fieldWithPath("imageUrl").description("프로필 이미지 Url"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("accountBank").description("은행명"),
                                fieldWithPath("accountNumber").description("계좌번호"),
                                fieldWithPath("phoneNumber").description("전화번호")
                        )
                ));
    }

}