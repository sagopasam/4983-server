package team.dankookie.server4983.book.repository.bookImage;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static team.dankookie.server4983.book.domain.QBookImage.bookImage;

@RequiredArgsConstructor
public class BookImageRepositoryImpl implements BookImageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public String getBookImageUrlByUsedBookId(Long usedBookId) {
        return queryFactory.select(bookImage.imageUrl)
                .from(bookImage)
                .where(bookImage.usedBook.id.eq(usedBookId))
                .orderBy(bookImage.id.desc())
                .limit(1)
                .fetchOne();
    }
}
