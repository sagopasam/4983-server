package team.dankookie.server4983.chat.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import team.dankookie.server4983.chat.constant.ContentType;
import team.dankookie.server4983.chat.dto.ChatListResponse;
import team.dankookie.server4983.chat.dto.ChatMessageResponse;
import team.dankookie.server4983.chat.service.ChatService;
import team.dankookie.server4983.common.BaseControllerTest;
import team.dankookie.server4983.jwt.constants.TokenDuration;
import team.dankookie.server4983.jwt.dto.AccessToken;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatListControllerTest extends BaseControllerTest {

    @MockBean
    ChatService chatService;

    @Test
    void accessToken으로_유저의_채팅_리스트를_리턴한다() throws Exception {
        //given
        ChatListResponse chatListResponse1 = ChatListResponse.of(1L, "사회과학통계방법", "안녕하세요", LocalDateTime.of(2023,9,22,12,30,12), false, "imageUrl");
        ChatListResponse chatListResponse2 = ChatListResponse.of(2L, "컴퓨터공학개론", "테스트입니다.", LocalDateTime.of(2023,5,10,12,30,12), true, "imageUrl");

        String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());

        when(chatService.getChatListWithAccessToken(any()))
                .thenReturn(List.of(chatListResponse1, chatListResponse2));
        //when
        ResultActions resultActions = mockMvc.perform(get(API + "/chat/list")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(
                        document("chat-list/success",
                                requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                                ),
                                responseFields(
                                        fieldWithPath("[].chatRoomId").description("채팅방 아이디"),
                                        fieldWithPath("[].usedBookName").description("채팅방 거래 책 이름"),
                                        fieldWithPath("[].message").description("채팅방의 마지막 메시지"),
                                        fieldWithPath("[].createdAt").description("채팅방의 마지막 메시지 시간"),
                                        fieldWithPath("[].isRead").description("채팅방의 읽음 여부"),
                                        fieldWithPath("[].imageUrl").description("채팅방의 이미지 url")
                                )
                        )
                );
    }

    @Test
    void 유저의_채팅_리스트를_리턴한다() throws Exception {
        //given
        final long chatRoomId = 1L;
        final String accessToken = jwtTokenUtils.generateJwtToken("nickname", tokenSecretKey.getSecretKey(), TokenDuration.ACCESS_TOKEN_DURATION.getDuration());

        ChatMessageResponse response1 = ChatMessageResponse.of(
                "test1",
                ContentType.BOOK_PURCHASE_START,
                LocalDateTime.of(2023, 9, 22, 12, 30, 12)
        );
        ChatMessageResponse response2 = ChatMessageResponse.of(
                "test1",
                ContentType.BOOK_PURCHASE_START,
                LocalDateTime.of(2023, 9, 22, 12, 30, 12)
        );

        when(chatService.getChattingData(anyLong(), any(AccessToken.class)))
                .thenReturn(List.of(response1, response2));

        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get(API + "/chat/{chatRoomId}", chatRoomId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
                .andDo(print());

        //then
        resultActions.andExpect(status().isOk())
                .andDo(document(
                        "chat/get-chatting-data/success",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("accessToken")
                        ),
                        pathParameters(
                                parameterWithName("chatRoomId").description("채팅방 id")
                        ),
                        responseFields(
                                fieldWithPath("[].message").description("채팅 메시지"),
                                fieldWithPath("[].contentType").description("채팅의 타입"),
                                fieldWithPath("[].createdAt").description("채팅을 보낸 시간")
                        )
                ));

    }

}