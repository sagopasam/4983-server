package team.dankookie.server4983.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.jwt.constants.TokenDuration;
import team.dankookie.server4983.member.service.MemberService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;

class MemberWithdrawControllerTest extends BaseControllerTest {

    @MockBean
    MemberService memberService;

    @Test
    void 회원을_탈퇴한다() throws Exception {
        //given
        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());
        String withdrawUrl = API + "/withdraw";
        when(memberService.checkMemberAndWithdraw(anyString()))
                .thenReturn(true);
        //when
        ResultActions resultActions = mockMvc.perform(patch(withdrawUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken))
                .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("회원 탈퇴가 완료되었습니다."))
                .andDo(document("my-pages/member-withdraw/success",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        )
                        )
                );
    }
}