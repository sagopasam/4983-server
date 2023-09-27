package team.dankookie.server4983.book.controller;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.ResultActions;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.service.MyPageBookPurchaseDetailListService;
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

class MyPageBookPurchaseDetailListControllerTest extends BaseControllerTest {

    @MockBean
    private MyPageBookPurchaseDetailListService myPageBookPurchaseDetailListService;

    @Test
    void 회원이_존재하지_않는경우_에러를_리턴한다() throws Exception{
        //given
        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());
        final BookStatus bookstatus = BookStatus.SOLD;

        when(myPageBookPurchaseDetailListService.getMyPageBookPurchaseDetailList(bookstatus, AccessToken.of(accessToken, "nickname")))
                .thenThrow(new IllegalArgumentException("존재하지 않는 회원입니다."));
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/my-pages/book-purchase-detail-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("bookStatus", String.valueOf(BookStatus.SOLD))
                        .header("Authorization", "Bearer "+accessToken))
                .andDo(print());
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(
                        document("my-pages/book-purchase-detail-list/fail",
                                RequestDocumentation.queryParameters(
                                        parameterWithName("bookStatus").description("거래완료 구분자")
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