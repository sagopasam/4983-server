package team.dankookie.server4983.member.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.member.service.MemberService;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class MemberWithdrawControllerTest extends BaseControllerTest {

    @MockBean
    MemberService memberService;

    @Test
    void 회원을_탈퇴한다() throws Exception {
        //given
        String withdrawUrl = API + "/withdraw";
        final String nickname = "testNickname";
        when(memberService.checkMemberAndWithdraw(nickname))
                .thenReturn(true);
        //when
        ResultActions resultActions = mockMvc.perform(get(withdrawUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .param("nickname", "testNickname"))
                .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("회원 탈퇴가 완료되었습니다."))
                .andDo(document("my-pages/member-withdraw/success",
                                queryParameters(
                                        parameterWithName("nickname").description("닉네임")
                                )
                        )
                );
    }
}