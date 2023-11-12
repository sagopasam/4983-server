package team.dankookie.server4983.book.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BookStatus {
    ALL("전체"),
    SALE("판매중"),
    TRADE("거래중"),
    SOLD("판매완료");

    private final String status;

}
