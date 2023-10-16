package team.dankookie.server4983.chat.constant;

import lombok.Getter;

@Getter
public enum ContentType {
    BOOK_PURCHASE_START , BOOK_PURCHASE_START_SELLER, BOOK_PURCHASE_START_BUYER, // SELLCHAT_1_1 판매자 구매 요청 step : 1
    BOOK_PURCHASE_WARNING, BOOK_PURCHASE_START_NOTIFY_SELLER, BOOK_PURCHASE_START_NOTIFY_BUYER,
    BOOK_PURCHASE_REQUEST , BOOK_PURCHASE_REQUEST_SELLER, BOOK_PURCHASE_REQUEST_BUYER , // SELLCHAT_2 판매자 구매 수락 step : 2
    BOOK_SALE_REJECTION , BOOK_SALE_REJECTION_SELLER ,BOOK_SALE_REJECTION_BUYER , // SELLCHAT_1_2 거래 거절
    PAYMENT_CONFIRMATION_COMPLETE , PAYMENT_CONFIRMATION_COMPLETE_SELLER, PAYMENT_CONFIRMATION_COMPLETE_BUYER, // SELLCHAT_4 입금 확인 step : 3
    BOOK_PLACEMENT_SET , BOOK_PLACEMENT_SET_SELLER , BOOK_PLACEMENT_SET_BUYER ,// SELLCHAT_5 서적 배치 설정 성공
    BOOK_PLACEMENT_COMPLETE , BOOK_PLACEMENT_COMPLETE_SELLER , BOOK_PLACEMENT_COMPLETE_BUYER , // SELLCHAT_5 서적 배치 완료 step : 4
    TRADE_COMPLETE , TRADE_COMPLETE_SELLER , TRADE_COMPLETE_BUYER , // SELLCHAT_6 거래 완료 step : 5
    // SELLCHAT 거래 중지 요청
    TRADE_STOP_REQUEST_BEFORE_DEPOSIT_BUYER , TRADE_STOP_REQUEST_BEFORE_DEPOSIT_SELLER ,
    TRADE_STOP_REQUEST_AFTER_DEPOSIT_BUYER , TRADE_STOP_REQUEST_AFTER_DEPOSIT_SELLER ,
    TRADE_STOP_REQUEST_AFTER_BOOK_PLACEMENT_BUYER , TRADE_STOP_REQUEST_AFTER_BOOK_PLACEMENT_SELLER ,
    TRADE_WARNING_SELLER , TRADE_WARNING_BUYER , // 경고
    CUSTOM_SELLER , CUSTOM_BUYER; // 커스텀 메세지
}

