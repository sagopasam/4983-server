package team.dankookie.server4983.chat.constant;

import lombok.Getter;

@Getter
public enum ContentType {
    BOOK_PURCHASE_REQUEST,
    BOOK_SALE_REJECTION,
    BOOK_SALE_AGREEMENT,
    PAYMENT_CONFIRMATION_COMPLETE,
    LOCKER_SELECTION_COMPLETE,
    BOOK_PLACEMENT_COMPLETE,
    TRADE_COMPLETE;
}

