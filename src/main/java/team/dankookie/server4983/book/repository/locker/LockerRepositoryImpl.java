package team.dankookie.server4983.book.repository.locker;

import static team.dankookie.server4983.book.domain.QLocker.locker;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.book.dto.LockerResponse;
import team.dankookie.server4983.book.dto.QLockerResponse;

@RequiredArgsConstructor
public class LockerRepositoryImpl implements LockerRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<LockerResponse> findByTradeDateBetweenAndIsExistsTrue(LocalDate startDate,
      LocalDate endDate) {
    return queryFactory.select(
            new QLockerResponse(locker.lockerNumber, locker.isExists)
        ).from(locker)
        .where(
            locker.tradeDate.between(startDate, endDate),
            locker.isExists.isTrue()
        )
        .fetch();
  }

}
