package team.dankookie.server4983.book.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.UsedBookListResponse;
import team.dankookie.server4983.book.service.UsedBookListService;
import team.dankookie.server4983.common.BaseControllerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsedBookListControllerTest extends BaseControllerTest {

    @MockBean
    private UsedBookListService usedBookListService;

    final String API = "/api/used-book-list";

    @Test
    void 서적의_모든_리스트를_반환한다() throws Exception {
        //given
        final boolean canBuyElseAll = false;

        UsedBookListResponse usedBookListResponse1 = UsedBookListResponse.builder()
                .usedBookId(1L)
                .imageUrl("imageUrl")
                .bookStatus(BookStatus.SALE)
                .name("책이름")
                .tradeAvailableDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .price(10000)
                .build();

        UsedBookListResponse usedBookListResponse2 = UsedBookListResponse.builder()
                .usedBookId(2L)
                .imageUrl("imageUrl")
                .bookStatus(BookStatus.TRADE)
                .name("책이름")
                .tradeAvailableDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .price(100000)
                .build();

        final List<UsedBookListResponse> usedBookListResponseList = List.of(
                usedBookListResponse1, usedBookListResponse2
        );

        when(usedBookListService.getUsedBookList(false))
                .thenReturn(usedBookListResponseList);

        //when
        ResultActions resultActions = mockMvc.perform(get(API)
                        .queryParam("canBuyElseAll", String.valueOf(canBuyElseAll)))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(
                        document("used-book-list/getUsedBookList/success",
                                queryParameters(
                                        parameterWithName("canBuyElseAll").description("구매 가능 여부")
                                ),
                                responseFields(
                                        fieldWithPath("[].usedBookId").description("책 게시글 id"),
                                        fieldWithPath("[].imageUrl").description("책 이미지"),
                                        fieldWithPath("[].bookStatus").description("책 상태"),
                                        fieldWithPath("[].name").description("책 이름"),
                                        fieldWithPath("[].tradeAvailableDate").description("거래 가능 날짜"),
                                        fieldWithPath("[].createdAt").description("책 등록 날짜"),
                                        fieldWithPath("[].price").description("책 가격")
                                )
                        ));

    }

    @Test
    void 서적을_단과대와_학과에따라_리스트로_반환한다() throws Exception {
        //given
        final String URL = API + "/college-and-department";
        final boolean canBuyElseAll = false;

        UsedBookListResponse usedBookListResponse1 = UsedBookListResponse.builder()
                .usedBookId(1L)
                .imageUrl("imageUrl")
                .bookStatus(BookStatus.SALE)
                .name("책이름")
                .tradeAvailableDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .price(10000)
                .build();

        UsedBookListResponse usedBookListResponse2 = UsedBookListResponse.builder()
                .usedBookId(2L)
                .imageUrl("imageUrl")
                .bookStatus(BookStatus.TRADE)
                .name("책이름")
                .tradeAvailableDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .price(100000)
                .build();

        final List<UsedBookListResponse> usedBookListResponseList = List.of(
                usedBookListResponse1, usedBookListResponse2
        );

        List<College> collegeList = List.of(College.ENGINEERING, College.EDUCATION);
        List<Department> departmentList = List.of(Department.COMPUTER, Department.SOFTWARE, Department.KOREAN);

        when(usedBookListService.getUsedBookList(collegeList, departmentList, canBuyElseAll))
                .thenReturn(usedBookListResponseList);

        //when
        ResultActions resultActions = mockMvc.perform(get(URL)
                        .param("college", "ENGINEERING,EDUCATION")
                        .param("department", "COMPUTER,SOFTWARE,KOREAN")
                        .param("canBuyElseAll", String.valueOf(canBuyElseAll))
                )
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(
                        document("used-book-list/getUsedBookListWithCollegeAndDepartment/success",
                                queryParameters(

                                        parameterWithName("college").description("단과대학 enum 리스트"),
                                        parameterWithName("department").description("학과 enum 리스트"),
                                        parameterWithName("canBuyElseAll").description("구매 가능 여부")
                                ),
                                responseFields(
                                        fieldWithPath("[].usedBookId").description("책 게시글 id"),
                                        fieldWithPath("[].imageUrl").description("책 이미지"),
                                        fieldWithPath("[].bookStatus").description("책 상태"),
                                        fieldWithPath("[].name").description("책 이름"),
                                        fieldWithPath("[].tradeAvailableDate").description("거래 가능 날짜"),
                                        fieldWithPath("[].createdAt").description("책 등록 날짜"),
                                        fieldWithPath("[].price").description("책 가격")
                                )
                        ));

    }



}