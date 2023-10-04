package team.dankookie.server4983.scheduler.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import team.dankookie.server4983.scheduler.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

import static team.dankookie.server4983.scheduler.entity.QSchedule.schedule;

@RequiredArgsConstructor
public class SchedulerRepositoryImpl implements CustomSchedulerRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Schedule> findByAlertTime(LocalDateTime time) {
        LocalDateTime start = time.withNano(0).minusMinutes(3);
        LocalDateTime end = time.withNano(0).plusMinutes(3);

        return jpaQueryFactory.select(schedule)
                .from(schedule)
                .where(schedule.time.goe(start).and(schedule.time.loe(end)))
                .fetch();
    }

}
