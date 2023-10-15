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
import team.dankookie.server4983.jwt.dto.AccessToken;
import team.dankookie.server4983.member.service.MemberService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberWithdrawControllerTest extends BaseControllerTest {

    @MockBean
    MemberService memberService;

    @Test
    void 회원을_탈퇴한다() throws Exception {
        //given
        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());
        String withdrawUrl = API + "/withdraw";
        when(memberService.checkMemberAndWithdraw(AccessToken.of(accessToken, "nickname")))
                .thenReturn(true);
        //when
        ResultActions resultActions = mockMvc.perform(patch(withdrawUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer "+ accessToken))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string("회원 탈퇴가 완료되었습니다."))
                .andDo(document("my-pages/member-withdraw/success",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        )
                        )
                );
    }
}