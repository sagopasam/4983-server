package team.dankookie.server4983.book.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import team.dankookie.server4983.book.service.MyPageBookSalesDetailListService;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.common.exception.ErrorResponse;
import team.dankookie.server4983.jwt.constants.TokenDuration;
import team.dankookie.server4983.jwt.dto.AccessToken;

import static io.jsonwebtoken.lang.Strings.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MyPageBookSalesDetailListControllerTest extends BaseControllerTest {

    @MockBean
    private MyPageBookSalesDetailListService myPageBookSalesDetailListService;


    @Test
    void 회원이_존재하지_않는경우_에러를_리턴한다() throws Exception{
        //given
        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());
        final boolean canBuy = true;

        when(myPageBookSalesDetailListService.getMyPageBookSalesDetailList(canBuy, AccessToken.of(accessToken, "nickname")))
                .thenThrow(new IllegalArgumentException("존재하지 않는 회원입니다."));
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/my-pages/book-sales-detail-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("canBuy", String.valueOf(canBuy))
                        .header("Authorization", "Bearer "+accessToken))
                .andDo(print());


       // then
        resultActions.andExpect(status().isBadRequest())
                .andDo(
                   document("my-pages/book-sales-detail-list/fail",
                           RequestDocumentation.queryParameters(
                                   parameterWithName("canBuy").description("판매중과 거래완료 구분자")
                           ),
                           requestHeaders(
                                   headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                           ),
                           responseFields(
                                   fieldWithPath("message").description("에러 메시지")
                           )
                    )
                );
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString(UTF_8);
        assertThat(contentAsString).isEqualTo(objectMapper.writeValueAsString(ErrorResponse.of("존재하지 않는 회원입니다.")));

    }
}