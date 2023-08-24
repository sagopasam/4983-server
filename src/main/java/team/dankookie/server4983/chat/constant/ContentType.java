package team.dankookie.server4983.chat.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    BOOK_PURCHASE_REQUEST(
            "'%s' 님이 거래 요청을 보냈어요!\n오늘 거래하러 갈래요?\n",
            "'%s' 님께 '%s' 서적 거래를 요청하였습니다.\n\n판매자의 응답을 기다려주세요:)\n"
    ),

    BOOK_SALE_REJECTION(
            "'%s' 님의 '%s' 서적 거래 요청을 거절하셨습니다.\n이후에도, 해당 서적 판매를 이어나가길 원하신다면, '네'를 클릭하여 거래 날짜를 수정해주세요.\n\n'아니오' 선택 시, 24시간 이내 해당 판매 게시글이 삭제됩니다,\n",
            "아쉽게도 '%s' 님과의 서적 거래가 이루어지지 못했습니다.\n"
    ),
    BOOK_SALE_AGREEMENT(
            "구매자에게 요청 수락 알림을 보냈습니다.\n입금이 확인 될 때 까지 기다려주세요:)\n",
            "'%s' 님께서 '%s' 서적 거래 요청을 수락하셨습니다.\n\n아래 계좌정보로 결제금액을 송금하여 주십시오.\n\n계좌번호:\n우리은행 1002-3597-18283 (사고파삼)\n결제 금액: %d원\n"
    ),
    PAYMENT_CONFIRMATION_COMPLETE(
            "입금이 확인되었습니다.\n구매자가 '거래 완료' 버튼을 클릭 후 판매 금액이 자동으로 입금될 예정입니다.\n\n책을 넣을 사물함을 선택하고 비밀번호를 설정해주세요.\n",
            "입금이 확인되었습니다.\n거래날짜에 판매자가 사물함에 서적을 배치할 예정입니다.\n"
    ),
    LOCKER_SELECTION_COMPLETE(
            "기입하셨던 거래 날짜에 맞게, 당일 내에 배치 해주시길 바랍니다.\n\n서적 배치 이후 완료 버튼을 눌러주세요.\n\n구매자가 배치된 서적을 수령한 후, '거래 완료' 버튼을 클릭하면 판매금액이 자동으로 입금됩니다.\n",
            ""
    ),
    BOOK_PLACEMENT_COMPLETE(
            "",
            "서적 배치가 완료되었습니다.\n금일 내 수령해주시길 바랍니다.\n\n'거래 완료' 버튼을 눌러야, 판매자에게 판매금액이 입금되오니, 수령 후 버튼을 꼭 눌러주세요\n\n사물함 번호: %d번\n사물함 비밀번호: %s\n"
    ),
    TRADE_COMPLETE(
            "거래가 완료되었습니다.\n이용해주셔서 감사합니다.\n\n-사고파삼-\n",
            "거래가 완료되었습니다.\n이용해주셔서 감사합니다.\n\n-사고파삼-\n"
    );

    private final String sellerContent;
    private final String buyerContent;
}
