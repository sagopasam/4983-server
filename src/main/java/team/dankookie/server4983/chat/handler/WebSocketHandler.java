package team.dankookie.server4983.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRequest chatMessage = objectMapper.readValue(message.getPayload(), ChatRequest.class);

        sendData(objectMapper , chatMessage);

    }

    private void sendData(ObjectMapper objectMapper, ChatRequest chatMessage) throws IOException {
        ChatResponse chatResponse = createChatResponse(chatMessage);
        String result = objectMapper.writeValueAsString(chatResponse);

        WebSocketSession receiver = sessionMap.get("세션 아이디를 넣어주세요.");

        if(receiver != null && receiver.isOpen()) {
            receiver.sendMessage(new TextMessage(result));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        sessionMap.put(sessionId, session);

        System.out.println("세션 시작 : " + session.getId());

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionMap.remove(session.getId());

        System.out.println("세션 종료 : " + session.getId());
    }

}
