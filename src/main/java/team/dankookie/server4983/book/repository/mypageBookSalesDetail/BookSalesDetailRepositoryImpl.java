package team.dankookie.server4983.book.repository.mypageBookSalesDetail;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;

import static team.dankookie.server4983.book.domain.QUsedBook.usedBook;

@RequiredArgsConstructor
public class BookSalesDetailRepositoryImpl implements BookSalesDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<UsedBook> getMyPageBookSalesDetailList(boolean canBuy, Long memberId) {
        JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);
        return getMyPageBooksCanBuy(canBuy, memberId,query);
    }

    private List<UsedBook> getMyPageBooksCanBuy(boolean canBuy, Long memberId, JPAQuery<UsedBook> query){
        if(canBuy){
            return query
                    .where(
                            usedBook.bookStatus.eq(BookStatus.SALE)
                        .and(
                            usedBook.sellerMember.id.eq(memberId)
                            ))
                    .orderBy(usedBook.createdAt.desc()).fetch();
        } else{
            return query
                    .where(
                            usedBook.bookStatus.eq(BookStatus.SOLD)
                        .and(
                            usedBook.sellerMember.id.eq(memberId)
                            ))
                    .orderBy(usedBook.createdAt.desc()).fetch();
        }

    }
}
