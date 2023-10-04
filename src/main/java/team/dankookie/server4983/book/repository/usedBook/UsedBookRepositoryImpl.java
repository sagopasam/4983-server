package team.dankookie.server4983.book.repository.usedBook;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.dto.QUsedBookListResponse;
import team.dankookie.server4983.book.dto.UsedBookListResponse;

import java.util.List;

import static team.dankookie.server4983.book.domain.QBookImage.bookImage;
import static team.dankookie.server4983.book.domain.QUsedBook.usedBook;

@RequiredArgsConstructor
public class UsedBookRepositoryImpl implements UsedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedBookListResponse> getUsedBookList(boolean isOrderByTradeAvailableDatetime) {
        JPAQuery<UsedBookListResponse> query = queryFactory.select(
                new QUsedBookListResponse(
                        usedBook.id,
                        JPAExpressions.select(bookImage.imageUrl)
                                .from(bookImage)
                                .where(bookImage.usedBook.eq(usedBook))
                                .orderBy(bookImage.id.asc())
                                .limit(1),
                        usedBook.bookStatus,
                        usedBook.name,
                        usedBook.tradeAvailableDatetime,
                        usedBook.createdAt,
                        usedBook.price
                )
        ).from(usedBook);

        return getUsedBooksIsOrderByTradeAvailableDatetime(isOrderByTradeAvailableDatetime, query);
    }

    @Override
    public List<UsedBookListResponse> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean isOrderByTradeAvailableDatetime) {
        JPAQuery<UsedBookListResponse> query = queryFactory.select(
                        new QUsedBookListResponse(
                                usedBook.id,
                                JPAExpressions.select(bookImage.imageUrl)
                                        .from(bookImage)
                                        .where(bookImage.usedBook.eq(usedBook))
                                        .orderBy(bookImage.id.asc())
                                        .limit(1),
                                usedBook.bookStatus,
                                usedBook.name,
                                usedBook.tradeAvailableDatetime,
                                usedBook.createdAt,
                                usedBook.price
                        )
                ).from(usedBook)
                .where(
                        usedBook.college.in(college).or(usedBook.department.in(department))
                );

        return getUsedBooksIsOrderByTradeAvailableDatetime(isOrderByTradeAvailableDatetime, query);
    }

    @Override
    public List<UsedBookListResponse> getUsedBookListBySearchKeyword(String searchKeyword, boolean isOrderByTradeAvailableDatetime) {
        JPAQuery<UsedBookListResponse> query = queryFactory.select(
                        new QUsedBookListResponse(
                                usedBook.id,
                                JPAExpressions.select(bookImage.imageUrl)
                                        .from(bookImage)
                                        .where(bookImage.usedBook.eq(usedBook))
                                        .orderBy(bookImage.id.asc())
                                        .limit(1),
                                usedBook.bookStatus,
                                usedBook.name,
                                usedBook.tradeAvailableDatetime,
                                usedBook.createdAt,
                                usedBook.price
                        )
                ).from(usedBook)
                .where(
                        usedBook.name.contains(searchKeyword)
                );
        return getUsedBooksIsOrderByTradeAvailableDatetime(isOrderByTradeAvailableDatetime, query);
    }

    private List<UsedBookListResponse> getUsedBooksIsOrderByTradeAvailableDatetime(boolean isOrderByTradeAvailableDatetime, JPAQuery<UsedBookListResponse> query) {
        if (isOrderByTradeAvailableDatetime) {
            return query
                    .where(usedBook.isDeleted.eq(false),
                            usedBook.sellerMember.isWithdraw.eq(false)
                    )
                    .orderBy(
                            new CaseBuilder()
                                    .when(usedBook.bookStatus.eq(BookStatus.SALE)).then(1)
                                    .when(usedBook.bookStatus.eq(BookStatus.TRADE)).then(2)
                                    .when(usedBook.bookStatus.eq(BookStatus.SOLD)).then(3)
                                    .otherwise(4)
                                    .asc(),
                            usedBook.tradeAvailableDatetime.desc()
                    ).fetch();
        }else {

            return query
                    .where(usedBook.isDeleted.eq(false),
                            usedBook.sellerMember.isWithdraw.eq(false)
                    )
                    .orderBy(
                            new CaseBuilder()
                                    .when(usedBook.bookStatus.eq(BookStatus.SALE)).then(1)
                                    .when(usedBook.bookStatus.eq(BookStatus.TRADE)).then(2)
                                    .when(usedBook.bookStatus.eq(BookStatus.SOLD)).then(3)
                                    .otherwise(4)
                                    .asc(),
                            usedBook.id.desc()
                    )
                    .fetch();
        }
    }
}
