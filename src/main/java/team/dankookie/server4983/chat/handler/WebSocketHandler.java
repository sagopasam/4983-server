package team.dankookie.server4983.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.chat.dto.ChatResponse;

import java.io.IOException;
import java.util.*;

import static team.dankookie.server4983.chat.dto.ChatResponse.createChatResponse;

@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessionMap = new HashMap<>();
    private final ChatLogicHandler chatLogicHandler;
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequest chatMessage = objectMapper.readValue(message.getPayload(), ChatRequest.class);

        System.out.println("받은 데이터 Type : " + chatMessage.getContentType());

        chatLogicHandler.chatLoginHandler(chatMessage , chatMessage.getData());
        sendData(chatMessage);
    }

    private void sendData(ChatRequest chatMessage) throws IOException {
        ChatResponse chatResponse = createChatResponse((String) chatMessage.getData().get("message") , chatMessage);
        String result = objectMapper.writeValueAsString(chatResponse);

        WebSocketSession receiver = sessionMap.get(chatMessage.getSession());

        if(receiver != null && receiver.isOpen()) {
            receiver.sendMessage(new TextMessage(result));
            System.out.println("메세지 전송 : " + chatMessage.getData().get("message"));
        } else {
            System.out.println("찾을 수 없는 사용자 : " + chatMessage.getSession());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessionMap.put(sessionId, session);

        System.out.println("세션 시작 : " + session.getId());
        String result = objectMapper.writeValueAsString(createChatResponse("성공적으로 연결되었습니다." , session.getId()));
        session.sendMessage(new TextMessage(result));

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());

        System.out.println("세션 종료 : " + session.getId());
    }

}
