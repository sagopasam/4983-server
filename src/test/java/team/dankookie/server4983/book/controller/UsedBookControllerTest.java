package team.dankookie.server4983.book.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.UsedBookResponse;
import team.dankookie.server4983.book.dto.UsedBookSaveRequest;
import team.dankookie.server4983.book.dto.UsedBookSaveResponse;
import team.dankookie.server4983.book.service.UsedBookService;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsedBookControllerTest extends BaseControllerTest {

    @MockBean
    UsedBookService usedBookService;

    @Test
    void 중고서적_데이터를_저장한다() throws Exception {
        //given
        final UsedBookSaveResponse response = UsedBookSaveResponse.of(1L);

        final List<MultipartFile> multipartFileList = List.of();
        final UsedBookSaveRequest usedBookSaveRequest = UsedBookSaveRequest.of(
                College.LAW,
                Department.BUSINESS,
                15000,
                LocalDate.of(2023, 9, 13),
                "책이름",
                "출판사",
                false,
                true,
                true
        );

        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), 1000L);

        MockMultipartFile file1 = new MockMultipartFile("fileList", "file1.png", MediaType.MULTIPART_FORM_DATA_VALUE, "file1".getBytes(UTF_8));
        MockMultipartFile file2 = new MockMultipartFile("fileList", "file2.png", MediaType.MULTIPART_FORM_DATA_VALUE, "file2".getBytes(UTF_8));
        MockMultipartFile file3 = new MockMultipartFile("fileList", "file3.png", MediaType.MULTIPART_FORM_DATA_VALUE, "file3".getBytes(UTF_8));
        MockMultipartFile file4 = new MockMultipartFile("fileList", "file4.png", MediaType.MULTIPART_FORM_DATA_VALUE, "file4".getBytes(UTF_8));

        List<MockMultipartFile> fileList = List.of(file1, file2, file3, file4);
        MockMultipartFile usedBook = new MockMultipartFile("usedBook", null, MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(usedBookSaveRequest).getBytes(UTF_8));
        when(usedBookService.saveAndSaveFiles(anyList(), any(UsedBookSaveRequest.class), any(AccessToken.class)))
                .thenReturn(UsedBookSaveResponse.of(1L));

        //when
        ResultActions resultActions = mockMvc.perform(
                multipart(API + "/used-book")
                        .file(file1)
                        .file(file2)
                        .file(file3)
                        .file(file4)
                        .file(usedBook)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        ).andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("used-book/save/success",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                                ),
                                requestParts(
                                        partWithName("fileList").description("파일 리스트"),
                                        partWithName("usedBook").description("중고서적 정보")
                                ),
                                requestPartFields(
                                        "usedBook",
                                        fieldWithPath("college").description("단과대"),
                                        fieldWithPath("department").description("학과"),
                                        fieldWithPath("price").description("가격"),
                                        fieldWithPath("tradeAvailableDate").description("거래 가능 날짜"),
                                        fieldWithPath("name").description("책 이름"),
                                        fieldWithPath("publisher").description("출판사"),
                                        fieldWithPath("isUnderlinedOrWrite").description("밑줄및 필기흔적 여부"),
                                        fieldWithPath("isDiscolorationAndDamage").description("페이지 변색 및 훼손 여부"),
                                        fieldWithPath("isCoverDamaged").description("겉표지 훼손 여부")
                                ),
                                responseFields(
                                        fieldWithPath("usedBookId").description("중고서적 id")
                                )
                        )
                );
    }

    @Test
    void 중고서적의_id로_중고서적_데이터를_리턴한다() throws Exception {
        //given
        final Long usedBookId = 1L;
        final UsedBookResponse usedBookResponse = UsedBookResponse.of(
                College.LAW.name(),
                Department.BUSINESS.name(),
                "nickname",
                "profileImageUrl",
                LocalDateTime.of(2023, 9, 13, 12, 0, 0),
                List.of("image1Url", "image2Url"),
                "책이름",
                "출판사",
                LocalDate.of(2023, 9, 13),
                false,
                true,
                true,
                15000,
                BookStatus.SALE
        );


        when(usedBookService.findByUsedBookId(usedBookId))
                .thenReturn(usedBookResponse);
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/used-book/{id}", usedBookId))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document("used-book/detail/success",
                        pathParameters(
                                parameterWithName("id").description("중고서적 id")
                        ),
                        responseFields(
                                fieldWithPath("college").description("단과대"),
                                fieldWithPath("department").description("학과"),
                                fieldWithPath("sellerUserNickname").description("판매자 닉네임"),
                                fieldWithPath("sellerProfileImageUrl").description("판매자 프로필 이미지"),
                                fieldWithPath("createdAt").description("등록일"),
                                fieldWithPath("bookImage").description("책 이미지 주소"),
                                fieldWithPath("bookName").description("책 이름"),
                                fieldWithPath("publisher").description("출판사"),
                                fieldWithPath("tradeAvailableDate").description("거래 가능 날짜"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("bookStatus").description("책 상태"),
                                fieldWithPath("underlinedOrWrite").description("밑줄및 필기흔적 여부"),
                                fieldWithPath("discolorationAndDamage").description("페이지 변색 및 훼손 여부"),
                                fieldWithPath("coverDamaged").description("겉표지 훼손 여부")
                        )
                ));

    }

}