package team.dankookie.server4983.chat.constant;

import lombok.Getter;

@Getter
public enum ContentType {
    BOOK_PURCHASE_START, // SELLCHAT_1_1 판매자 구매 요청 step : 1
    BOOK_PURCHASE_REQUEST, // SELLCHAT_2 판매자 구매 수락 step : 2
    BOOK_SALE_REJECTION, // SELLCHAT_1_2 거래 거절
    PAYMENT_CONFIRMATION_COMPLETE, // SELLCHAT_4 입금 확인 step : 3
    BOOK_PLACEMENT_SET , // SELLCHAT_5 서적 배치 설정 성공
    BOOK_PLACEMENT_COMPLETE, // SELLCHAT_5 서적 배치 완료 step : 4
    TRADE_COMPLETE, // SELLCHAT_6 거래 완료 step : 5
    TRADE_STOP_REQUEST, // SELLCHAT 거래 중지 요청
    TRADE_WARNING , // 경고
    CUSTOM; // 커스텀 메세지
}

