package team.dankookie.server4983.chat.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.dankookie.server4983.chat.dto.ChatRequest;
import team.dankookie.server4983.jwt.util.JwtTokenUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ChatLogicHandler {

    @Value("${jwt.secret-key}")
    private String key;

    public String chatLoginHandler(ChatRequest chatRequest , Map<String , Object> data) {
        switch(chatRequest.getContentType()) {
            case BOOK_PURCHASE_START:
                return purchaseBookStart(data);
            case BOOK_PURCHASE_REQUEST:

            case BOOK_SALE_REJECTION:

            case BOOK_SALE_AGREEMENT:

            case PAYMENT_CONFIRMATION_COMPLETE:

            case LOCKER_SELECTION_COMPLETE:

            case BOOK_PLACEMENT_COMPLETE:

            case TRADE_COMPLETE:

            default:
                return "잘못된 데이터 요청입니다.";
        }
    }

    public String purchaseBookStart(Map<String , Object> data) {
        String token = getAccessToken((String) data.get("Authentication"));
        String userName = JwtTokenUtils.getUsername(token , key);

        return String.format("\'%s\' 님이 거래 요청을 보냈어요!\n오늘 거래하러 갈래요?" , userName);
    }

    public String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

}
