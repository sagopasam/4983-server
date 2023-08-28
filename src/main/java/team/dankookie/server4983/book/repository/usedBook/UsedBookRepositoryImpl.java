package team.dankookie.server4983.book.repository.usedBook;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.constant.BookStatus;
import team.dankookie.server4983.book.constant.College;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.book.domain.UsedBook;

import java.util.List;

import static team.dankookie.server4983.book.domain.QUsedBook.usedBook;

@RequiredArgsConstructor
public class UsedBookRepositoryImpl implements UsedBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<UsedBook> getUsedBookList(boolean canBuy) {
        JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);
        return getUsedBooksCanBuyOrNot(canBuy, query);
    }

    @Override
    public List<UsedBook> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean canBuy) {
        JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);

        if (!college.isEmpty() && !department.isEmpty()) {
            query.where(usedBook.college.in(college).and(usedBook.department.in(department)));
        } else if (!college.isEmpty()) {
            query.where(usedBook.college.in(college));
        } else if (!department.isEmpty()) {
            query.where(usedBook.department.in(department));
        }

        return getUsedBooksCanBuyOrNot(canBuy, query);
    }

    private List<UsedBook> getUsedBooksCanBuyOrNot(boolean canBuy, JPAQuery<UsedBook> query) {
        if (canBuy) {
            return query
                    .where(usedBook.bookStatus.eq(BookStatus.SALE))
                    .orderBy(usedBook.createdAt.desc()).fetch();
        }else {
            return query
                    .orderBy(
                            new CaseBuilder()
                                    .when(usedBook.bookStatus.eq(BookStatus.SALE)).then(1)
                                    .when(usedBook.bookStatus.eq(BookStatus.TRADE)).then(2)
                                    .when(usedBook.bookStatus.eq(BookStatus.SOLD)).then(3)
                                    .otherwise(4)
                                    .asc(),
                            usedBook.createdAt.desc()
                    )
                    .fetch();
        }
    }
}
