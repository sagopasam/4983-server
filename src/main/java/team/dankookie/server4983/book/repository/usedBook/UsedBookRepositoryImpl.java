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
    public List<UsedBook> getUsedBookList(boolean canBuyElseAll) {
        JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);
        return getUsedBooksCanBuyElseAll(canBuyElseAll, query);
    }

    @Override
    public List<UsedBook> getUsedBookListInCollegeAndDepartment(List<College> college, List<Department> department, boolean canBuyElseAll) {
        JPAQuery<UsedBook> query = queryFactory.selectFrom(usedBook);

        query.where(usedBook.college.in(college).or(usedBook.department.in(department)));
        return getUsedBooksCanBuyElseAll(canBuyElseAll, query);
    }

    private List<UsedBook> getUsedBooksCanBuyElseAll(boolean canBuyElseAll, JPAQuery<UsedBook> query) {
        if (canBuyElseAll) {
            return query
                    .where(usedBook.bookStatus.eq(BookStatus.SALE).and(usedBook.isDeleted.eq(false)))
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
