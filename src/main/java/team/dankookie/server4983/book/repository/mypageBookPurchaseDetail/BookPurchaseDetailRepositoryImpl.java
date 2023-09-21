package team.dankookie.server4983.book.repository.mypageBookPurchaseDetail;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;
import java.util.Objects;

import static team.dankookie.server4983.book.domain.QUsedBook.usedBook;

@RequiredArgsConstructor
public class BookPurchaseDetailRepositoryImpl implements BookPurchaseDetailRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedBook> getMyPageBookPurchaseDetailList(BookStatus bookStatus, Long memberId){
    JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);
    return getMyPageBookStatus(bookStatus, memberId, query);
}

    private List<UsedBook> getMyPageBookStatus(BookStatus bookStatus, Long memberId, JPAQuery<UsedBook> query) {
        if (Objects.requireNonNull(bookStatus) == BookStatus.SOLD) {
            return query
                    .where(
                            usedBook.buyerMember.id.eq(memberId)
                    )
                    .orderBy(usedBook.createdAt.desc()).fetch();
        }
        return null;
    }
}